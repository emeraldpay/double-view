package io.emeraldpay.doubleview.spring.webflux;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import io.emeraldpay.doubleview.WebContext;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubleView implements View {

    private final DoubleViewRenderer renderer;
    private final List<MediaType> supportedMediaTypes = List.of(MediaType.TEXT_HTML);
    private final String componentName;
    private final Scheduler scheduler;
    private final List<String> attributes;

    public DoubleView(DoubleViewRenderer renderer, Scheduler scheduler, String componentName) {
        this(renderer, scheduler, componentName, Collections.emptyList());
    }

    public DoubleView(DoubleViewRenderer renderer, Scheduler scheduler, String componentName, List<String> attributes) {
        this.renderer = renderer;
        this.scheduler = scheduler;
        this.componentName = componentName;
        this.attributes = attributes != null ? attributes : Collections.emptyList();
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    @Override
    public Mono<Void> render(Map<String, ?> model, MediaType contentType, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatusCode.valueOf(200));
        return response.writeWith(Mono.fromCallable( () -> {
            // Create WebContext from exchange attributes (WebFlux doesn't have HttpServletRequest)
            Map<String, Object> contextAttributes = new HashMap<>(attributes.size());
            for (String attribute : attributes) {
                contextAttributes.put(attribute, exchange.getAttribute(attribute));
            }
            WebContext webContext = WebContext.of(contextAttributes);

            Map<String, Object> props = new HashMap<>(model.size());
            props.putAll(model);

            String rendered = renderer.render(componentName, props, webContext);
            return DefaultDataBufferFactory.sharedInstance.wrap(rendered.getBytes());
        }).subscribeOn(scheduler));
    }
}
