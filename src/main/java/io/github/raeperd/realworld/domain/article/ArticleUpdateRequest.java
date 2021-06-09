package io.github.raeperd.realworld.domain.article;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ArticleUpdateRequest {

    private final ArticleTitle titleToUpdate;
    private final String descriptionToUpdate;
    private final String bodyToUpdate;

    public static ArticleUpdateRequestBuilder builder() {
        return new ArticleUpdateRequestBuilder();
    }

    Optional<ArticleTitle> getTitleToUpdate() {
        return ofNullable(titleToUpdate);
    }

    Optional<String> getDescriptionToUpdate() {
        return ofNullable(descriptionToUpdate);
    }

    Optional<String> getBodyToUpdate() {
        return ofNullable(bodyToUpdate);
    }

    private ArticleUpdateRequest(ArticleUpdateRequestBuilder builder) {
        this(builder.titleToUpdate, builder.descriptionToUpdate, builder.bodyToUpdate);
    }

    private ArticleUpdateRequest(ArticleTitle titleToUpdate, String descriptionToUpdate, String bodyToUpdate) {
        this.titleToUpdate = titleToUpdate;
        this.descriptionToUpdate = descriptionToUpdate;
        this.bodyToUpdate = bodyToUpdate;
    }

    public static class ArticleUpdateRequestBuilder {
        private ArticleTitle titleToUpdate;
        private String descriptionToUpdate;
        private String bodyToUpdate;

        public ArticleUpdateRequestBuilder titleToUpdate(ArticleTitle titleToUpdate) {
            this.titleToUpdate = titleToUpdate;
            return this;
        }
        public ArticleUpdateRequestBuilder descriptionToUpdate(String descriptionToUpdate) {
            this.descriptionToUpdate = descriptionToUpdate;
            return this;
        }
        public ArticleUpdateRequestBuilder bodyToUpdate(String bodyToUpdate) {
            this.bodyToUpdate = bodyToUpdate;
            return this;
        }

        public ArticleUpdateRequest build() {
            return new ArticleUpdateRequest(this);
        }
    }
}
