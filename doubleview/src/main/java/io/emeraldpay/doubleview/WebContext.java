package io.emeraldpay.doubleview;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.graalvm.polyglot.HostAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * Context object for passing context data (like request attributes) to React components.
 */
public class WebContext {
    
    /**
     * Map of request attributes accessible from JavaScript.
     * Public field to allow direct property access in GraalVM.
     */
    @JsonProperty("attributes")
    @HostAccess.Export
    public final Map<String, Object> attributes;

    /**
     * Create WebContext from a Map of attributes
     */
    public static WebContext of(Map<String, Object> attributes) {
        return new WebContext(attributes);
    }

    /**
     * Create empty WebContext
     */
    public static WebContext empty() {
        return new WebContext(new HashMap<>());
    }
    
    public WebContext(Map<String, Object> attributes) {
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
    }
    
    /**
     * Get all attributes. 
     * Fallback method access if GraalVM decides to use it instead of direct field access.
     */
    @HostAccess.Export
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    /**
     * Get a specific attribute value by key.
     * Convenience method for server-side JavaScript access (but inaccessible from React)
     */
    @HostAccess.Export
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WebContext that = (WebContext) obj;
        return attributes.equals(that.attributes);
    }
    
    @Override
    public int hashCode() {
        return attributes.hashCode();
    }
    
    @Override
    public String toString() {
        return "WebContext[attributes=" + attributes + "]";
    }
}