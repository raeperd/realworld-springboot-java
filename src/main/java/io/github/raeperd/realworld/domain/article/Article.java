package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;
import static javax.persistence.GenerationType.IDENTITY;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @CreatedBy
    @JoinColumn
    @ManyToOne(targetEntity = User.class)
    private User author;

    @OneToMany(fetch = FetchType.LAZY)
    private final Collection<Tag> tagList = new HashSet<>();

    private String title;
    private String description;
    private String body;

    protected Article() {
    }

    public Article(String title, String description, String body) {
        this(title, description, body, emptySet());
    }

    public Article(String title, String description, String body, Set<Tag> tagList) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList.addAll(tagList);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public Collection<Tag> getTagList() {
        return tagList;
    }
}
