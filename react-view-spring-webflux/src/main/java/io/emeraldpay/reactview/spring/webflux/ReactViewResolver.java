package io.emeraldpay.reactview.spring.webflux;

import io.emeraldpay.reactview.ReactViewsRenderer;
import io.emeraldpay.reactview.ReactViewsRendererConfiguration;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class ReactViewResolver implements ViewResolver {

    private final ReactViewsRenderer renderer;

    public ReactViewResolver(ReactViewsRendererConfiguration configuration) {
        renderer = new ReactViewsRenderer(configuration);
    }

    public ReactViewResolver(ReactViewsRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Mono<View> resolveViewName(String viewName, Locale locale) {
        return Mono.just(new ReactView(renderer, viewName));
    }

}
