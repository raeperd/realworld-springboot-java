package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.tag.Tag;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;

@Embeddable
public class ArticleContents {

    @Embedded
    private ArticleTitle title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String body;

    @JoinTable(name = "articles_tags",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false))
    @ManyToMany(fetch = EAGER, cascade = PERSIST)
    private Set<Tag> tags = new HashSet<>();

    public ArticleContents(String description, ArticleTitle title, String body, Set<Tag> tags) {
        this.description = description;
        this.title = title;
        this.body = body;
        this.tags = tags;
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
