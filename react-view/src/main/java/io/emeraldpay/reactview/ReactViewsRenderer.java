package io.emeraldpay.reactview;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Supplier;

public class ReactViewsRenderer {

    private final ReactViewsRendererConfiguration configuration;
    private PropsHandler propsHandler;
    private ClientCode clientCode;
    private Supplier<HTMLGenerator> htmlGenerator;
    private Supplier<String> headGenerator;

    public ReactViewsRenderer(ReactViewsRendererConfiguration configuration) {
        this.configuration = configuration;
        clientCode = new ClientCode(configuration);
        try {
            htmlGenerator = HTMLTemplate.newBuilder().fromPath(configuration.getHtmlTemplatePath()).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String render(String componentName, Map<String, Object> props) {
        try (JSContext context = new JSContext(configuration)) {
            RenderCallback callback = new RenderCallback();

            Map<String, Object> finalProps;
            if (propsHandler != null) {
                finalProps = propsHandler.apply(props);
            } else {
                finalProps = props;
            }

            Value jsProps = context.polyglotContext.asValue(ProxyObject.fromMap(finalProps));
            context.render.execute(
                    context.ssrModule,
                    configuration.getModuleName(),
                    componentName,
                    jsProps,
                    callback
            );

            String ssrHTML = callback.getHtml();

            HTMLGenerator generator = htmlGenerator.get();
            if (headGenerator != null) {
                generator.addHead(headGenerator.get());
            }
            generator.setViewBody(ssrHTML);
            generator.addPostBody(clientCode.generateScripts(componentName, finalProps));

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

    public Supplier<String> getHeadGenerator() {
        return headGenerator;
    }

    public void setHeadGenerator(Supplier<String> headGenerator) {
        this.headGenerator = headGenerator;
    }

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
