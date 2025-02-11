package io.emeraldpay.doubleview.spring.webflux;

import io.emeraldpay.doubleview.DoubleViewRenderer;
import io.emeraldpay.doubleview.DoubleViewRendererConfiguration;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Locale;

public class DoubleViewResolver implements ViewResolver {

    private final DoubleViewRenderer renderer;

    /**
     * Scheduler to use for rendering. It's a CPU bound task, and also it's a resource-heavy task
     * so it's better to use a [possibly dedicated] scheduler for that.
     */
    private Scheduler scheduler = Schedulers.parallel();

    public DoubleViewResolver(DoubleViewRendererConfiguration configuration) {
        renderer = new DoubleViewRenderer(configuration);
    }

    public DoubleViewResolver(DoubleViewRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Mono<View> resolveViewName(String viewName, Locale locale) {
        return Mono.just(new DoubleView(renderer, scheduler, viewName));
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * @param scheduler scheduler used for rendering.
     */
    public void setScheduler(Scheduler scheduler) {
        if (scheduler == null) {
            throw new IllegalArgumentException("Scheduler cannot be null");
        }
        this.scheduler = scheduler;
    }
}
