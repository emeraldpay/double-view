package io.emeraldpay.doubleview.ktor

import io.emeraldpay.doubleview.DoubleViewRenderer
import io.emeraldpay.doubleview.WebContext
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.server.application.*
import io.ktor.server.application.hooks.BeforeResponseTransform
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
val DoubleView: ApplicationPlugin<PluginConfiguration> = createApplicationPlugin(
    "Doubleview",
    ::PluginConfiguration
) {

    val renderer = pluginConfig.renderer ?: DoubleViewRenderer(pluginConfig.configuration
        ?: throw IllegalArgumentException("DoubleView Renderer or Configuration must be provided"))
    val useAttributes = pluginConfig.requestAttributes.sorted()
    val dispatcher = pluginConfig.dispatcher

    @OptIn(InternalAPI::class)
    on(BeforeResponseTransform(DoubleViewContent::class)) { call, content ->
        // 1. Get WebContext attributes from call
        // ------------------------------------
        val contextAttributes = mutableMapOf<String, Any?>()
        // we go through the attributes instead of getting them by name because AttributeKey is typed
        for (key in call.attributes.allKeys) {
            if (useAttributes.binarySearch(key.name) >= 0) {
                val value = call.attributes.getOrNull(key)
                if (value != null) {
                    contextAttributes[key.name] = value
                }
            }
        }
        val webContext = WebContext.of(contextAttributes)
        // ------------------------------------

        // 2. Render
        // ------------------------------------
        // Rendered prepares a new GraalVM context for each thread on first use in that thread, and it takes time to init.
        // It must be called from a CPU-bound dispatcher
        val html = withContext(dispatcher) {
            renderer.render(content.viewName, content.props, webContext)
        }
        // ------------------------------------

        TextContent(
            text = html,
            contentType = ContentType.Text.Html,
            status = HttpStatusCode.OK,
        )
    }
}
