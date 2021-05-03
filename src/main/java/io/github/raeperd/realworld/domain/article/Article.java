package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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

    Article updateArticle(ArticleUpdateCommand updateCommand) {
        updateCommand.getTitleToUpdate().ifPresent(titleToUpdate -> this.title = titleToUpdate);
        updateCommand.getDescriptionToUpdate().ifPresent(descriptionToUpdate -> this.description = descriptionToUpdate);
        updateCommand.getBodyToUpdate().ifPresent(bodyToUpdate -> this.body = bodyToUpdate);
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var article = (Article) o;
        return Objects.equals(id, article.id) && Objects.equals(title, article.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
