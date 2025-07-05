package io.emeraldpay.doubleview.spring.web;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import io.emeraldpay.doubleview.WebContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubleView implements View {

    private final DoubleViewRenderer renderer;
    private final String componentName;
    private final List<String> attributes;

    public DoubleView(DoubleViewRenderer renderer, String componentName) {
        this(renderer, componentName, Collections.emptyList());
    }

    public DoubleView(DoubleViewRenderer renderer, String componentName, List<String> attributes) {
        this.renderer = renderer;
        this.componentName = componentName;
        this.attributes = attributes != null ? attributes : Collections.emptyList();
    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> contextAttributes = HashMap.newHashMap(attributes.size());
        for (String attribute : attributes) {
            contextAttributes.put(attribute, request.getAttribute(attribute));
        }
        WebContext webContext = WebContext.of(contextAttributes);

        Map<String, Object> props = HashMap.newHashMap(model.size());
        props.putAll(model);

        String rendered = renderer.render(componentName, props, webContext);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null && status instanceof Integer) {
            response.setStatus((int) status);
        } else {
            response.setStatus(200);
        }
        response.getOutputStream().write(rendered.getBytes());
    }

}
