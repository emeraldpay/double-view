package io.emeraldpay.doubleview;

import java.util.Map;
import java.util.Objects;

/**
 * All details used for rendering a React View.
 */
public class RenderContext {

    /**
     * Name of the React component to render.
     */
    private final String componentName;

    /**
     * Properties to pass to the React component.
     */
    private final Map<String, Object> props;

    private final WebContext webContext;

    public RenderContext(String componentName, Map<String, Object> props, WebContext webContext) {
        this.componentName = componentName;
        this.props = props;
        this.webContext = webContext;
    }

    public String getComponentName() {
        return componentName;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public WebContext getWebContext() {
        return webContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RenderContext that)) return false;
        return Objects.equals(componentName, that.componentName) && Objects.equals(props, that.props);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentName, props);
    }
}
