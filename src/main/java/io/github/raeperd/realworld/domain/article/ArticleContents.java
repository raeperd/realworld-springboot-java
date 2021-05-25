package io.github.raeperd.realworld.domain.article;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class ArticleContents {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String body;

    protected ArticleContents() {
    }
}
