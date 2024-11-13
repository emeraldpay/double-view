package io.emeraldpay.doubleview.spring.webflux;

import io.emeraldpay.doubleview.DoubleViewsRenderer;
import io.emeraldpay.doubleview.DoubleViewsRendererConfiguration;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

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
    public Mono<View> resolveViewName(String viewName, Locale locale) {
        return Mono.just(new DoubleView(renderer, viewName));
    }

}
