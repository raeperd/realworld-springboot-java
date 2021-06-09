package io.github.raeperd.realworld.domain.article;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ArticleTitle {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String slug;

    public static ArticleTitle of(String title) {
        return new ArticleTitle(title, slugFromTitle(title));
    }

    private ArticleTitle(String title, String slug) {
        this.title = title;
        this.slug = slug;
    }

    protected ArticleTitle() {
    }

    private static String slugFromTitle(String title) {
        return title.toLowerCase()
                .replaceAll("\\$,'\"|\\s|\\.|\\?", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("(^-)|(-$)", "");
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleTitle that = (ArticleTitle) o;
        return slug.equals(that.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug);
    }
}
