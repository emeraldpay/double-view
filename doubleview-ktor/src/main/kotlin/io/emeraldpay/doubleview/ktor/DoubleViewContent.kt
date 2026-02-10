package io.emeraldpay.doubleview.ktor

import io.ktor.http.HttpStatusCode

data class DoubleViewContent(
    val viewName: String,
    val props: Map<String, Any?>,
    val status: HttpStatusCode? = null
)