package io.emeraldpay.doubleview.spring.web;

import io.emeraldpay.doubleview.DoubleViewsRenderer;
import io.emeraldpay.doubleview.DoubleViewsRendererConfiguration;
import org.springframework.web.servlet.View;
import  org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class DoubleViewResolver implements ViewResolver {

    private final DoubleViewsRenderer renderer;

    public DoubleViewResolver(DoubleViewsRendererConfiguration configuration) {
        renderer = new DoubleViewsRenderer(configuration);
    }

    public DoubleViewResolver(DoubleViewsRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) {
        return new DoubleView(renderer, viewName);
    }

}
