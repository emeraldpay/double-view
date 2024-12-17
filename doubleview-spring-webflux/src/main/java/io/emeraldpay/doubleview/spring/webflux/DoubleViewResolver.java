package io.emeraldpay.doubleview.spring.webflux;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import io.emeraldpay.doubleview.DoubleViewRendererConfiguration;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

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
    public Mono<View> resolveViewName(String viewName, Locale locale) {
        return Mono.just(new DoubleView(renderer, viewName));
    }

}
