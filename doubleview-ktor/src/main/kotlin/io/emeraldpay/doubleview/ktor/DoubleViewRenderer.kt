package io.emeraldpay.doubleview.ktor

import io.emeraldpay.doubleview.WebContext
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Renders a React component using DoubleView SSR and responds with HTML.
 *
 * This function:
 * 1. Extracts configured attributes from the call
 * 2. Executes server-side rendering on a separate dispatcher (CPU-bound operation)
 * 3. Responds with HTML content
 *
 * @param componentName The name of the React component to render
 * @param props The properties to pass to the React component
 */
suspend fun ApplicationCall.renderView(
    componentName: String,
    props: Map<String, Any?>
) {
    val renderer = application.doubleViewRenderer

    // Get WebContext attributes from call
    val contextAttributes = mutableMapOf<String, Any?>()
    val useAttributes = application.doubleViewAttributes
    // we go through the attributes instead of getting them by name because AttributeKey is typed
    for (key in attributes.allKeys) {
        if (useAttributes.binarySearch(key.name) >= 0) {
            val value = attributes.getOrNull(key)
            if (value != null) {
                contextAttributes[key.name] = value
            }
        }
    }

    val webContext = WebContext.of(contextAttributes)

    // Rendered prepares a new GraalVM context for each thread on first use in that thread, and it takes time to init.
    // So it must be called from a CPU-bound dispatcher to avoid unnecessary initialization delays on render
    val html = withContext(Dispatchers.Default) {
        renderer.render(componentName, props, webContext)
    }

    respondText(html, ContentType.Text.Html, HttpStatusCode.OK)
}

/**
 * Alternative rendering function that accepts a suspend function to build props.
 * Useful when props need to be fetched asynchronously.
 *
 * @param componentName The name of the React component to render
 * @param propsBuilder Suspend function that builds the props map
 */
suspend fun ApplicationCall.renderView(
    componentName: String,
    propsBuilder: suspend () -> Map<String, Any?>
) {
    val props = propsBuilder()
    renderView(componentName, props)
}
