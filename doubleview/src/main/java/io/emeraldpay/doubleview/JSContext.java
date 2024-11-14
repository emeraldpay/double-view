package io.emeraldpay.doubleview;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Contains the JS context for rendering React components.
 * <br/>
 * <b>WARNING</b>: it's not thread safe instance because it contains the state of the execution and should not be re-used.
 */
class JSContext implements AutoCloseable {

    private final Context polyglotContext;
    private final Value renderer;
    private final Value components;

    /**
     * See Builder
     *
     * @param context GraalVM execution context
     * @param components JS script with the components to render
     * @param renderer JS script to render the components on the server
     */
    private JSContext(Context context, Source components, Source renderer) {
        this.polyglotContext = context;
        this.components = context.eval(components);

        Value global = context.getBindings("js");
        for (String name : this.components.getMemberKeys()) {
            global.putMember(name, this.components.getMember(name));
        }

        Value renderModule = context.eval(renderer);
        this.renderer = renderModule.getMember("ssr");
    }

    @Override
    public void close() {
        polyglotContext.close();
    }

    public Context getPolyglotContext() {
        return polyglotContext;
    }

    public Value getRenderer() {
        return renderer;
    }

    public Value getComponents() {
        return components;
    }

    public static class Builder {
        private final Source bundle;
        private final Source render;
        private final Context.Builder polyglotContext;

        Builder(DoubleViewsRendererConfiguration configuration) throws IOException {
            Engine.Builder engine = Engine.newBuilder("js")
                    .out(System.out)
                    .err(System.err);

            if (!configuration.usesOptimization()) {
                // GraalVM produces a warning if there is a no compilation support, which is okay as we intentionally do not try to enable it
                engine = engine.option("engine.WarnInterpreterOnly", "false");
            }

            bundle = loadSource(configuration.getServerBundlePath());
            render = loadSource(configuration.getRendererScript());

            polyglotContext = Context.newBuilder("js")
                    .engine(engine.build())
                    .allowAllAccess(true)
                    .option("js.esm-eval-returns-exports", "true")
                    .option("js.unhandled-rejections", "throw");
        }

        public Source loadSource(String path) throws IOException {
            if (path.startsWith(".") || path.startsWith("/")) {
                path = "file:" + path;
            }

            String source;
            String fileName;

            if (path.startsWith("classpath:")) {
                String resourcePath = path.substring("classpath:".length());
                fileName = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
                try (var stream = getClass().getResourceAsStream(resourcePath)) {
                    if (stream == null) {
                        throw new IllegalArgumentException("Resource not found: " + resourcePath);
                    }
                    source = new String(stream.readAllBytes());
                }
            } else if (path.startsWith("file:")) {
                Path filePath = Path.of(path.substring("file:".length()));
                fileName = filePath.getFileName().toString();
                source = Files.readString(filePath);
            } else {
                throw new IllegalArgumentException("Invalid path: " + path);
            }

            return Source.newBuilder("js", source, fileName)
                    .mimeType("application/javascript+module")
                    .build();
        }

        /**
         * Creates a fresh state for execution. Should be called each time the code is executed.
         * @return a new JSContext
         */
        public JSContext build() {
            return new JSContext(polyglotContext.build(), bundle, render);
        }

    }
}
