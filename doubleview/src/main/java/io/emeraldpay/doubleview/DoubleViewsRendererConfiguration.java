package io.emeraldpay.doubleview;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DoubleViewsRendererConfiguration {

    /**
     * Public URL of the client bundle. It is used to load the client bundle in the browser.
     */
    private String clientBundleURL = "/static/client.js";

    /**
     * JS code for Server Side Rendering
     */
    private String serverBundlePath = "classpath:/views/ssr-components.mjs";

    /**
     * Render Script
     */
    private String renderScript = "classpath:/doubleview/render.js";

    private boolean sandbox = false;

    private String moduleName = "doubleview";

    /**
     * A mapper used to convert the props to JSON for a client-side processing.
     */
    private ObjectMapper objectMapper;

    /**
     * Path to the HTML template file.
     * @see HTMLTemplate
     * @see HTMLGenerator
     */
    private String htmlTemplatePath = "classpath:/doubleview/template.html";

    /**
     * Enable JS compilation.
     * That required the application to be run with GraalVM (Oracle's or Community Edition), or with a special configuration for an embeddable JS compiler.
     * Please see https://www.graalvm.org/latest/reference-manual/embed-languages/#runtime-optimization-support
     * If set to `false` then it disables the optimization, which may be useful for development on OpenJDK and other JVMs.
     */
    private boolean useOptimization = true;

    public String getClientBundleURL() {
        return clientBundleURL;
    }

    public void setClientBundleURL(String clientBundleURL) {
        this.clientBundleURL = clientBundleURL;
    }

    public String getServerBundlePath() {
        return serverBundlePath;
    }

    public void setServerBundlePath(String serverBundlePath) {
        this.serverBundlePath = serverBundlePath;
    }

    public String getRenderScript() {
        return renderScript;
    }

    public void setRenderScript(String renderScript) {
        this.renderScript = renderScript;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public boolean usesOptimization() {
        return useOptimization;
    }

    public void enableOptimization(boolean useOptimization) {
        this.useOptimization = useOptimization;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getHtmlTemplatePath() {
        return htmlTemplatePath;
    }

    public void setHtmlTemplatePath(String htmlTemplatePath) {
        this.htmlTemplatePath = htmlTemplatePath;
    }
}
