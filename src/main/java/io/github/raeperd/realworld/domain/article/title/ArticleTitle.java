package io.github.raeperd.realworld.domain.article.title;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ArticleTitle {

    private static final String SLUG_TRANSFORM_REGEX = "\\$,'\"|\\s|\\.|\\?";

    private String title;

    public static ArticleTitle of(String title) {
        return new ArticleTitle(title);
    }

    private ArticleTitle(String title) {
        this.title = title;
    }

    protected ArticleTitle() {
    }

    Slug toSlug() {
        return Slug.of(this);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleTitle that = (ArticleTitle) o;
        return title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    static class Slug {

        private final String value;

        private static Slug of(ArticleTitle articleTitle) {
            return new Slug(articleTitle.toString()
                    .toLowerCase()
                    .replaceAll(SLUG_TRANSFORM_REGEX, "-"));
        }

        private Slug(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
