package io.emeraldpay.reactview;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ReactViewsRenderer {

    private final ReactViewsRendererConfiguration configuration;
    private PropsHandler propsHandler;

    public ReactViewsRenderer(ReactViewsRendererConfiguration configuration) {
        this.configuration = configuration;
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

            return callback.getHtml();
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
