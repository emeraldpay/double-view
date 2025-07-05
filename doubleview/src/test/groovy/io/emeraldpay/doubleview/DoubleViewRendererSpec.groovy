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

    def "should render WebContextTest component with webContext data"() {
        setup:
        def webContextConfig = new DoubleViewRendererConfiguration()
        webContextConfig.setModuleName("doubleview-test-web-context")
        webContextConfig.setServerBundlePath("./src/test/js/build/web-context-test.js")
        webContextConfig.setObjectMapper(new ObjectMapper())
        webContextConfig.setDevMode(true)
        webContextConfig.enableOptimization(false)
        
        def renderer = new DoubleViewRenderer(webContextConfig)
        def props = [
            title: "Test WebContext"
        ]
        
        def webContext = WebContext.of([
            userId: "user123",
            userRole: "admin",
            requestPath: "/dashboard"
        ])

        when:
        def html = renderer.render("WebContextTest", props, webContext)
                .replaceAll("<!--\\s*-->", "")

        then:
        html != null
        html.contains("Test WebContext")
        html.contains("User ID: <span class=\"user-id\">user123</span>")
        html.contains("User Role: <span class=\"user-role\">admin</span>")
        html.contains("Request Path: <span class=\"request-path\">/dashboard</span>")
        html.contains('<div class="web-context-test">')
        html.contains("This component demonstrates accessing webContext data via React Context.")
    }

    def "should render WebContextTest component with empty webContext"() {
        setup:
        def webContextConfig = new DoubleViewRendererConfiguration()
        webContextConfig.setModuleName("doubleview-test-web-context")
        webContextConfig.setServerBundlePath("./src/test/js/build/web-context-test.js")
        webContextConfig.setObjectMapper(new ObjectMapper())
        webContextConfig.setDevMode(true)
        webContextConfig.enableOptimization(false)
        
        def renderer = new DoubleViewRenderer(webContextConfig)
        def props = [
            title: "Empty Context Test"
        ]
        
        def webContext = WebContext.empty()

        when:
        def html = renderer.render("WebContextTest", props, webContext)
                .replaceAll("<!--\\s*-->", "")

        then:
        html != null
        html.contains("Empty Context Test")
        html.contains("User ID: <span class=\"user-id\">N/A</span>")
        html.contains("User Role: <span class=\"user-role\">N/A</span>")
        html.contains("Request Path: <span class=\"request-path\">N/A</span>")
        html.contains('<div class="web-context-test">')
    }
}