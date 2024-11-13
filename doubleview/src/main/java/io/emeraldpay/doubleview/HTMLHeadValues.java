package io.emeraldpay.doubleview;

import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;

public class HTMLHeadValues implements Function<RenderContext, String> {

    private final Iterable<Function<RenderContext, String>> source;

    private HTMLHeadValues(Iterable<Function<RenderContext, String>> source) {
        this.source = source;
    }

    @Override
    public String apply(RenderContext renderContext) {
        StringBuilder builder = new StringBuilder();
        for (Function<RenderContext, String>value: source) {
            builder.append(value.apply(renderContext));
        }
        return builder.toString();
    }

    public static class Builder {
        private final List<Function<RenderContext, String>> source = new ArrayList<>();

        public Builder add(Function<RenderContext, String> value) {
            source.add(value);
            return this;
        }

        /**
         * Use a constant value for the title
         * @param title the title
         * @return this
         */
        public Builder title(String title) {
            return add(new JustString("<title>" + title + "</title>"));
        }

        /**
         * Use a dynamic value for the title, which may use the details from the RenderContext.
         * Function must return just a string to put inside the title tag.
         *
         * @param title the title
         * @return this
         */
        public Builder title(Function<RenderContext, String> title) {
            Function<RenderContext, String> wrapped = title.andThen((value) -> "<title>" + value + "</title>");
            return add(wrapped);
        }

        /**
         * Add a stylesheet to the head
         *
         * @param href the URL of the stylesheet
         * @return this
         */
        public Builder stylesheet(String href) {
            return add(new JustString("<link rel=\"stylesheet\" href=\"" + href + "\">"));
        }

        public HTMLHeadValues build() {
            return new HTMLHeadValues(source);
        }
    }

    static class JustString implements Function<RenderContext, String> {

        private final String value;

        public JustString(String value) {
            this.value = value;
        }

        @Override
        public String apply(RenderContext renderContext) {
            return value;
        }
    }
}
