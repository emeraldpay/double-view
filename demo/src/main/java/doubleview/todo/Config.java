package doubleview.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.emeraldpay.doubleview.DoubleViewRenderer;
import io.emeraldpay.doubleview.DoubleViewRendererConfiguration;
import io.emeraldpay.doubleview.HTMLHeadValues;
import io.emeraldpay.doubleview.PropsHandler;
import io.emeraldpay.doubleview.spring.web.DoubleViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class Config implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ObjectMapper objectMapper = new ObjectMapper();

        DoubleViewRendererConfiguration config = new DoubleViewRendererConfiguration();
        // set the name same as defined in `vite.shared.ts`
        config.setModuleName("doubleview-demo-todo");
        config.enableOptimization(false);
        config.setObjectMapper(objectMapper);
        config.setClientBundleURL("/client.js");
        config.setServerBundlePath("./build/doubleview/server/assets/server.js");

        DoubleViewRenderer renderer = new DoubleViewRenderer(config);
        renderer.setHeadGenerator(new HTMLHeadValues.Builder()
                .title("DoubleView Demo")
                .stylesheet("/styles.css")
                .build()
        );
        // just to show what it uses for rendering
        renderer.setPropsHandler(new PropsHandler.LoggingHandler());

        registry.viewResolver(new DoubleViewResolver(renderer));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/*.js", "/*.js.map", "/*.css")
                .addResourceLocations("file:./build/doubleview/client/assets/")
                .setCacheControl(CacheControl.noCache());
    }
}
