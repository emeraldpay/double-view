package io.emeraldpay.doubleview;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class DoubleViewRenderer {

    private final DoubleViewRendererConfiguration configuration;
    private PropsHandler propsHandler;
    private ClientCode clientCode;
    private Supplier<HTMLGenerator> htmlGenerator;
    private Function<RenderContext, String> headGenerator;

    private final JSContextProvider jsContextProvider;

    public DoubleViewRenderer(DoubleViewRendererConfiguration configuration) {
        this.configuration = configuration;
        clientCode = new ClientCode(configuration);
        try {
            htmlGenerator = HTMLTemplate.newBuilder().fromPath(configuration.getHtmlTemplatePath()).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jsContextProvider = new JSContextProvider(configuration);
    }

    public String render(String componentName, Map<String, Object> props) {
        try (JSContext context = jsContextProvider.get()) {
            RenderCallback callback = new RenderCallback();

            Map<String, Object> finalProps;
            if (propsHandler != null) {
                finalProps = propsHandler.apply(props);
            } else {
                finalProps = props;
            }

            Value jsProps = context.getPolyglotContext().asValue(ProxyObject.fromMap(finalProps));
            context.getRenderer().execute(
                    context.getComponents(),
                    configuration.getModuleName(),
                    componentName,
                    jsProps,
                    callback
            );

            RenderContext renderContext = new RenderContext(componentName, finalProps);

            String ssrHTML = callback.getHtml();

            HTMLGenerator generator = htmlGenerator.get();
            if (headGenerator != null) {
                generator.addHead(headGenerator.apply(renderContext));
            }
            generator.setViewBody(ssrHTML);
            generator.addPostBody(clientCode.generateScripts(renderContext));

            return generator.generate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to render component " + componentName, e);
        }
    }

    public PropsHandler getPropsHandler() {
        return propsHandler;
    }

    public void setPropsHandler(PropsHandler propsHandler) {
        this.propsHandler = propsHandler;
    }

    public ClientCode getClientCode() {
        return clientCode;
    }

    public void setClientCode(ClientCode clientCode) {
        this.clientCode = clientCode;
    }

    public Supplier<HTMLGenerator> getHtmlGenerator() {
        return htmlGenerator;
    }

    public void setHtmlGenerator(Supplier<HTMLGenerator> htmlGenerator) {
        this.htmlGenerator = htmlGenerator;
    }

    public Function<RenderContext, String> getHeadGenerator() {
        return headGenerator;
    }

    public void setHeadGenerator(Function<RenderContext, String> headGenerator) {
        this.headGenerator = headGenerator;
    }

    /**
     * Gets the results back from the Renderer script.
     */
    public static final class RenderCallback {
        private String html = "<div>Not Rendered</div>";

        RenderCallback() {
        }

        @HostAccess.Export
        public void setHTML(String html) {
            this.html = html;
        }

        @HostAccess.Export
        public void setHTML(int[] unsignedBytes) {
            byte[] bytes = new byte[unsignedBytes.length];
            for (int i = 0; i < unsignedBytes.length; i++) {
                bytes[i] = (byte) unsignedBytes[i];
            }
            setHTML(new String(bytes, StandardCharsets.UTF_8));
        }

        public String getHtml() {
            return html;
        }
    }
}
