package io.emeraldpay.doubleview.spring.web;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import io.emeraldpay.doubleview.DoubleViewRendererConfiguration;
import org.springframework.web.servlet.View;
import  org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class DoubleViewResolver implements ViewResolver {

    private final DoubleViewRenderer renderer;
    private final DoubleViewRendererConfiguration configuration;

    public DoubleViewResolver(DoubleViewRendererConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("DoubleViewRendererConfiguration cannot be null");
        }
        this.configuration = configuration;
        this.renderer = new DoubleViewRenderer(configuration);
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) {
        return new DoubleView(renderer, viewName, configuration.getRequestAttributes());
    }

}
