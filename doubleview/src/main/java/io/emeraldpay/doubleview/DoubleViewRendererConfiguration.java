package io.emeraldpay.doubleview;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DoubleViewRendererConfiguration {

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
    private String rendererScript = "classpath:/doubleview/render.js";

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

    /**
     * Enable development mode.
     * In development mode, the renderer loads the code each time before rendering. Which allows to see the changes without restarting the application.
     */
    private boolean devMode = false;

    /**
     * List of request attribute names that should be passed to React components as WebContext.
     * By default, no attributes are passed.
     */
    private List<String> requestAttributes = new ArrayList<>();

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

    public String getRendererScript() {
        return rendererScript;
    }

    public void setRendererScript(String rendererScript) {
        this.rendererScript = rendererScript;
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

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void setDevMode() {
        setDevMode(true);
    }

    public List<String> getRequestAttributes() {
        return requestAttributes;
    }

    public void setRequestAttributes(List<String> requestAttributes) {
        this.requestAttributes = requestAttributes != null ? requestAttributes : new ArrayList<>();
    }

}
