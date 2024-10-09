package io.emeraldpay.reactview;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class JSContext implements AutoCloseable {
    Context polyglotContext;
    Value render;
    Value ssrModule;

    JSContext(ReactViewsRendererConfiguration configuration) throws IOException {
        var engineBuilder = Engine.newBuilder("js")
                .out(System.out)
                .err(System.err);

        if (!configuration.usesOptimization()) {
            // GraalVM produces a warning if there is a no compilation support, which is okay as we intentionally do not try to enable it
            engineBuilder = engineBuilder.option("engine.WarnInterpreterOnly", "false");
        }

        polyglotContext = Context.newBuilder("js")
                .engine(engineBuilder.build())
                .allowAllAccess(true)
                .option("js.esm-eval-returns-exports", "true")
                .option("js.unhandled-rejections", "throw")
                .build();

        Value global = polyglotContext.getBindings("js");
        ssrModule = polyglotContext.eval(loadSource(configuration.getServerBundlePath()));

        for (String name : ssrModule.getMemberKeys()) {
            global.putMember(name, ssrModule.getMember(name));
        }

        Source renderSource = loadSource(configuration.getRenderScript());
        Value renderModule = polyglotContext.eval(renderSource);
        render = renderModule.getMember("ssr");
    }

    private Source loadSource(String path) throws IOException {
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

    @Override
    public void close() {
        polyglotContext.close();
    }
}
