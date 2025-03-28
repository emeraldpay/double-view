package io.emeraldpay.doubleview.spring.web;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

import java.util.Map;

public class DoubleView implements View {

    private final DoubleViewRenderer renderer;
    private final String componentName;

    public DoubleView(DoubleViewRenderer renderer, String componentName) {
        this.renderer = renderer;
        this.componentName = componentName;
    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rendered = renderer.render(componentName, (Map<String, Object>) model);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null && status instanceof Integer) {
            response.setStatus((int) status);
        } else {
            response.setStatus(200);
        }
        response.getOutputStream().write(rendered.getBytes());
    }

}
