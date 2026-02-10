package io.emeraldpay.doubleview.ktor

data class DoubleViewContent(
    val viewName: String,
    val props: Map<String, Any?>
)