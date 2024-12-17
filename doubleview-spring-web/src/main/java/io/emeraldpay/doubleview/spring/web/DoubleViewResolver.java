package io.emeraldpay.doubleview.spring.web;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import io.emeraldpay.doubleview.DoubleViewRendererConfiguration;
import org.springframework.web.servlet.View;
import  org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class DoubleViewResolver implements ViewResolver {

    private final DoubleViewRenderer renderer;

    public DoubleViewResolver(DoubleViewRendererConfiguration configuration) {
        renderer = new DoubleViewRenderer(configuration);
    }

    public DoubleViewResolver(DoubleViewRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) {
        return new DoubleView(renderer, viewName);
    }

}
