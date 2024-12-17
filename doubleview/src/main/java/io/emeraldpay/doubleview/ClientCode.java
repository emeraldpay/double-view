package io.emeraldpay.doubleview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generate the client-side code to initialize the React components.
 */
public class ClientCode {

    private String clientBundleURL = "/static/client.js";
    private ObjectMapper objectMapper;

    public ClientCode() {
    }

    public ClientCode(DoubleViewRendererConfiguration configuration) {
        this.clientBundleURL = configuration.getClientBundleURL();
        this.objectMapper = configuration.getObjectMapper();
    }

    public String getClientBundleURL() {
        return clientBundleURL;
    }

    public void setClientBundleURL(String clientBundleURL) {
        this.clientBundleURL = clientBundleURL;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Generate the client-side code to initialize the React components.
     * It includes both the loader for the client bundle and the initialization code.
     *
     * @param renderContext Render context
     * @return JavaScript code
     */
    public String generateScripts(RenderContext renderContext) {
        StringBuilder builder = new StringBuilder();
        builder.append(generateLoader());
        builder.append(generateInit(renderContext));
        return builder.toString();
    }

    /**
     * Generate HTML code to loads the client bundle.
     *
     * @return HTML code
     */
    public String generateLoader() {
        return "<script type=\"text/javascript\" src=\"" + clientBundleURL + "\"></script>";
    }

    /**
     * Generate HTML code with data necessary to initialize the React component on the client side.
     *
     * @param renderContext Render context
     * @return HTML code
     */
    public String generateInit(RenderContext renderContext) {
        String propsJson;
        try {
            propsJson = objectMapper.writeValueAsString(renderContext.getProps());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            propsJson = "{}";
        }
        return "<script type=\"text/javascript\">"
                + "window.doubleView = window.doubleView || {};"
                + "window.doubleView.props = " + propsJson + ";"
                + "window.doubleView.component = '" + renderContext.getComponentName() + "';"
                + "</script>";
    }
}
