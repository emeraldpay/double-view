package io.emeraldpay.reactview;

import java.io.PrintWriter;
import java.util.Map;
import java.util.function.Function;

public interface PropsHandler extends Function<Map<String, Object>, Map<String, Object>> {

    class LoggingHandler implements PropsHandler {

        private final PrintWriter out;

        public LoggingHandler(PrintWriter out) {
            this.out = out;
        }

        public LoggingHandler() {
            this(new PrintWriter(System.out));
        }

        @Override
        public Map<String, Object> apply(Map<String, Object> props) {
            out.println("props count: " + props.size());
            for (Map.Entry<String, Object> entry : props.entrySet()) {
                out.println(". key  : " + entry.getKey());
                out.println("  value: " + entry.getValue());
            }
            out.flush();
            return props;
        }
    }
}
