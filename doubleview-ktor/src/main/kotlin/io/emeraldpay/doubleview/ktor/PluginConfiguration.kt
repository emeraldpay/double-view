package io.emeraldpay.doubleview.ktor


import io.emeraldpay.doubleview.DoubleViewRenderer
import io.emeraldpay.doubleview.DoubleViewRendererConfiguration

class PluginConfiguration {

    /**
     * DoubleView renderer configuration. Required if no rendered is set.
     */
    var renderer: DoubleViewRenderer? = null
        get() = field
        set(value) {
            require(configuration == null) { "Cannot set both renderer and configuration" }
            field = value
        }


    /**
     * Pre-configured renderer instance. If not provided, will be created from the configuration.
     */
    var configuration: DoubleViewRendererConfiguration? = null
        get() = field
        set(value) {
            require(renderer == null) { "Cannot set both renderer and configuration" }
            field = value
        }

    /**
     * List of request attribute keys to pass to React components via WebContext.
     * The attribute values should be set using call.attributes.put() before rendering.
     */
    var requestAttributes: List<String> = emptyList()


}