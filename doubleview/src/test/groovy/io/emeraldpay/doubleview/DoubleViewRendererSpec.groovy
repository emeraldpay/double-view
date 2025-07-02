package io.emeraldpay.doubleview

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class DoubleViewRendererSpec extends Specification {

    def config = new DoubleViewRendererConfiguration()

    def setup() {
        config.setModuleName("doubleview-test-hello-world")
        config.setServerBundlePath("./src/test/js/build/hello-world.js")
        config.setObjectMapper(new ObjectMapper())
        config.setDevMode(true)
        config.enableOptimization(false)
    }

    def "should render HelloWorld component with default props"() {
        setup:
        def renderer = new DoubleViewRenderer(config)
        def props = [:]

        when:
        def html = renderer.render("HelloWorld", props)
            .replaceAll("<!--\\s*-->", "")

        then:
        html != null
        html.contains("Hello, World!")
        html.contains("This is a simple test component for DoubleView rendering.")
        html.contains('<div class="hello-world">')
    }

    def "should render HelloWorld component with custom props"() {
        setup:
        def renderer = new DoubleViewRenderer(config)
        def props = [
            message: "Greetings",
            name: "Spock"
        ]

        when:
        def html = renderer.render("HelloWorld", props)
                .replaceAll("<!--\\s*-->", "")

        then:
        html != null
        html.contains("Greetings, Spock!")
        html.contains("This is a simple test component for DoubleView rendering.")
        html.contains('<div class="hello-world">')
    }
}