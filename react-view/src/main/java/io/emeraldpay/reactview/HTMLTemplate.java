package io.emeraldpay.reactview;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;

/**
 *
 * An HTML generator based on a template where part to replace are marked with placeholders like {@code <!-- REACT:HEAD -->}.
 * A default template is in the resources as {@see resources/reactview/template.html}
 *
 */
public class HTMLTemplate implements Supplier<HTMLGenerator> {

    private final List<TemplatePart> template;

    private HTMLTemplate(String template) {
        Pattern pattern = Pattern.compile("<!--\\s*REACT:(\\w+)\\s*-->");
        Matcher matcher = pattern.matcher(template);
        int lastEnd = 0;

        List<TemplatePart> parts = new ArrayList<>();

        while (matcher.find()) {
            // Add HTML content before the placeholder
            if (matcher.start() > lastEnd) {
                parts.add(new TemplatePart(Part.HTML, template.substring(lastEnd, matcher.start())));
            }

            // Add the placeholder
            String placeholderType = matcher.group(1);
            Part part = switch (placeholderType) {
                case "HEAD" -> Part.PLACE_HEAD;
                case "BODY" -> Part.PLACE_BODY;
                case "POST_BODY" -> Part.PLACE_POST_BODY;
                default -> throw new IllegalArgumentException("Unknown placeholder type: " + placeholderType);
            };
            parts.add(new TemplatePart(part, ""));

            lastEnd = matcher.end();
        }

        // Add any remaining HTML content after the last placeholder
        if (lastEnd < template.length()) {
            parts.add(new TemplatePart(Part.HTML, template.substring(lastEnd)));
        }

        this.template = parts;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public HTMLGenerator get() {
        return new Generator(this);
    }

    private record TemplatePart(Part type, String content) {}

    private enum Part {
        /**
         * HTML to pu to output as is
         */
        HTML,

        /**
         * Placeholder for the head section
         */
        PLACE_HEAD,
        /**
         * Placeholder for the body section
         */
        PLACE_BODY,
        /**
         * Placeholder for the post-body section
         */
        PLACE_POST_BODY
    }

    static class Generator implements HTMLGenerator {
        private final HTMLTemplate template;

        public Generator(HTMLTemplate template) {
            this.template = template;
        }

        private final StringBuilder headContent = new StringBuilder();
        private String bodyContent = "";
        private final StringBuilder postBodyContent = new StringBuilder();

        @Override
        public void addHead(String head) {
            headContent.append(head);
        }

        @Override
        public void setViewBody(String body) {
            bodyContent = body;
        }

        @Override
        public void addPostBody(String postBody) {
            postBodyContent.append(postBody);
        }

        @Override
        public String generate() {
            StringBuilder result = new StringBuilder();
            for (TemplatePart part : template.template) {
                switch (part.type()) {
                    case HTML:
                        result.append(part.content());
                        break;
                    case PLACE_HEAD:
                        result.append(headContent);
                        break;
                    case PLACE_BODY:
                        result.append(bodyContent);
                        break;
                    case PLACE_POST_BODY:
                        result.append(postBodyContent);
                        break;
                }
            }
            return result.toString();
        }
    }

    static class Builder {

        private String template;

        /**
         * Create a new builder from the specified path. It detects if the path is a classpath resource or a file.
         *
         * @param path
         * @return
         */
        public Builder fromPath(String path) throws IOException {
            if (path.startsWith("classpath:")) {
                return fromClasspath(path);
            } else {
                return fromFile(path);
            }
        }

        /**
         * Load HTML template from the classpath
         *
         * @param resource
         * @return
         */
        public Builder fromClasspath(String resource) throws IllegalArgumentException, IOException {
            if (resource.startsWith("classpath:")) {
                resource = resource.substring("classpath:".length());
            }
            try (InputStream inputStream = Builder.class.getResourceAsStream(resource)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("Resource not found: " + resource);
                }
                byte[] bytes = inputStream.readAllBytes();
                this.template = new String(bytes, StandardCharsets.UTF_8);
            }
            return this.fromString(template);
        }

        public Builder fromFile(String file) throws IOException {
            if (file.startsWith("file:")) {
                file = file.substring("file:".length());
            }
            this.template = Files.readString(Paths.get(file));
            return this.fromString(template);
        }

        public Builder fromString(String template) {
            this.template = template;
            return this;
        }

        public HTMLTemplate build() {
            return new HTMLTemplate(template);
        }
    }
}
