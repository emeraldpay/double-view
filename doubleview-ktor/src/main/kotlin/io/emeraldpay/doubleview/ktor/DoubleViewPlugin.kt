package io.emeraldpay.doubleview.ktor

import io.emeraldpay.doubleview.DoubleViewRenderer
import io.emeraldpay.doubleview.DoubleViewRendererConfiguration
import io.ktor.server.application.*
import io.ktor.util.*

/**
 * Ktor plugin for DoubleView React SSR rendering.
 *
 * Example usage:
 * ```
 * install(DoubleView) {
 *     configuration = myDoubleViewConfig
 *     requestAttributes = listOf("locale", "userId")
 * }
 * ```
 */
class DoubleViewPlugin(internal val config: Configuration) {

    class Configuration {
        /**
         * DoubleView renderer configuration. Required.
         */
        var configuration: DoubleViewRendererConfiguration? = null

        /**
         * Pre-configured renderer instance. If not provided, will be created from configuration.
         */
        var renderer: DoubleViewRenderer? = null

        /**
         * List of request attribute keys to pass to React components via WebContext.
         * These attributes should be set using call.attributes.put() before rendering.
         */
        var requestAttributes: List<String> = emptyList()
    }

    companion object Plugin : BaseApplicationPlugin<Application, Configuration, DoubleViewPlugin> {
        override val key = AttributeKey<DoubleViewPlugin>("DoubleView")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): DoubleViewPlugin {
            val config = Configuration().apply(configure)

            val rendererConfig = config.configuration
                ?: throw IllegalArgumentException("DoubleViewRendererConfiguration must be provided")

            val renderer = config.renderer ?: DoubleViewRenderer(rendererConfig)

            val plugin = DoubleViewPlugin(config)

            // Store renderer in application attributes for easy access
            pipeline.attributes.put(DoubleViewRendererKey, renderer)
            pipeline.attributes.put(DoubleViewAttributesKey, config.requestAttributes.sorted())

            return plugin
        }
    }
}

/**
 * Attribute key for accessing the DoubleViewRenderer instance
 */
val DoubleViewRendererKey = AttributeKey<DoubleViewRenderer>("DoubleViewRenderer")

/**
 * Attribute key for accessing the list of request attributes to pass to WebContext
 */
val DoubleViewAttributesKey = AttributeKey<List<String>>("DoubleViewAttributes")


/**
 * Get the DoubleViewRenderer from the application
 */
val Application.doubleViewRenderer: DoubleViewRenderer
    get() = attributes[DoubleViewRendererKey]

/**
 * Get the list of request attributes configured for DoubleView
 */
val Application.doubleViewAttributes: List<String>
    get() = attributes.getOrNull(DoubleViewAttributesKey) ?: emptyList()
