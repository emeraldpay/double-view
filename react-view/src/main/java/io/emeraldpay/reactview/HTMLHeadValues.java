package io.emeraldpay.reactview;

import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

public class HTMLHeadValues implements Supplier<String> {

    private final Iterable<Supplier<String>> source;

    private HTMLHeadValues(Iterable<Supplier<String>> source) {
        this.source = source;
    }

    @Override
    public String get() {
        StringBuilder builder = new StringBuilder();
        for (Supplier<String> value: source) {
            builder.append(value.get());
        }
        return builder.toString();
    }

    public static class Builder {
        private final List<Supplier<String>> source = new ArrayList<>();

        public Builder add(Supplier<String> value) {
            source.add(value);
            return this;
        }

        public Builder title(String title) {
            return add(new JustString("<title>" + title + "</title>"));
        }

        public Builder stylesheet(String href) {
            return add(new JustString("<link rel=\"stylesheet\" href=\"" + href + "\">"));
        }

        public HTMLHeadValues build() {
            return new HTMLHeadValues(source);
        }
    }

    static class JustString implements Supplier<String> {

        private final String value;

        public JustString(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }
    }
}
