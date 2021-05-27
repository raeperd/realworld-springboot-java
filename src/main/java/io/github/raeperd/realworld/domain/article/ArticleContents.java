package io.github.raeperd.realworld.domain.article;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class ArticleContents {

    @Embedded
    private ArticleTitle title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String body;

    public ArticleContents(String description, ArticleTitle title, String body) {
        this.description = description;
        this.title = title;
        this.body = body;
    }

    protected ArticleContents() {
    }

    public ArticleTitle getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }
}
