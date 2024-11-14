package io.emeraldpay.doubleview;

/**
 * A JSContext provider for the current thread.
 */
public class JSContextProvider {

    private final ThreadLocal<JSContext.Builder> context;

    public JSContextProvider(DoubleViewsRendererConfiguration configuration) {
        context = ThreadLocal.withInitial(() -> {
            try {
                return new JSContext.Builder(configuration);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create JSContext", e);
            }
        });
    }

    public JSContext get() {
        JSContext.Builder value = context.get();
        return value.build();
    }

}
