package io.github.raeperd.realworld.domain.article;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class ArticleTitle {

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

    String getSlug() {
        return slug;
    }
}
