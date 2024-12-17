package io.emeraldpay.doubleview.spring.webflux;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public class DoubleView implements View {

    private final DoubleViewRenderer renderer;
    private final List<MediaType> supportedMediaTypes = List.of(MediaType.TEXT_HTML);
    private final String componentName;

    public DoubleView(DoubleViewRenderer renderer, String componentName) {
        this.renderer = renderer;
        this.componentName = componentName;
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
            String rendered = renderer.render(componentName, (Map<String, Object>) model);
            return DefaultDataBufferFactory.sharedInstance.wrap(rendered.getBytes());
        }));
    }
}
