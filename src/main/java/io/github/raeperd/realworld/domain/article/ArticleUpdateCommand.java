package io.github.raeperd.realworld.domain.article;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ArticleUpdateCommand {

    private final String title;
    private final String description;
    private final String body;

    public static class Builder {

        private String title;
        private String description;
        private String body;

        private Builder() {
            // no essentia`l fields
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public ArticleUpdateCommand build() {
            return new ArticleUpdateCommand(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private ArticleUpdateCommand(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.body = builder.body;
    }

    public Optional<String> getTitleToUpdate() {
        return ofNullable(title);
    }

    public Optional<String> getDescriptionToUpdate() {
        return ofNullable(description);
    }

    public Optional<String> getBodyToUpdate() {
        return ofNullable(body);
    }

}
