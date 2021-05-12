package io.github.raeperd.realworld.domain.article.title;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ArticleTitle {

    private String title;

    public static ArticleTitle of(String title) {
        return new ArticleTitle(title);
    }

    private ArticleTitle(String title) {
        this.title = title;
    }

    protected ArticleTitle() {
    }

    public Slug toSlug() {
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

}
