package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.article.tag.Tag;
import io.github.raeperd.realworld.domain.article.title.ArticleTitle;
import io.github.raeperd.realworld.domain.article.title.Slug;
import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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
    @ManyToOne
    private User author;

    @OneToMany(cascade = CascadeType.ALL)
    private final Collection<Tag> tagList = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private final Collection<Comment> comments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Embedded
    private ArticleTitle title;
    @Embedded
    private Slug slug;
    private String description;
    private String body;

    protected Article() {
    }

    public Article(ArticleTitle title, String description, String body) {
        this(title, description, body, emptySet());
    }

    public Article(ArticleTitle title, String description, String body, Set<Tag> tagList) {
        this.title = title;
        this.slug = title.toSlug();
        this.description = description;
        this.body = body;
        this.tagList.addAll(tagList);
    }

    Article updateArticle(ArticleUpdateCommand updateCommand) {
        updateCommand.getTitleToUpdate().ifPresent(titleToUpdate -> {
            title = ArticleTitle.of(titleToUpdate);
            slug = title.toSlug(); });
        updateCommand.getDescriptionToUpdate().ifPresent(descriptionToUpdate -> description = descriptionToUpdate);
        updateCommand.getBodyToUpdate().ifPresent(bodyToUpdate -> body = bodyToUpdate);
        return this;
    }

    public boolean deleteCommentByIdAndUser(long id, User user) {
        return comments.removeIf(comment -> comment.getId() == id && comment.isAuthor(user));
    }

    public Comment addComment(Comment comment) {
        comments.add(comment);
        return comment;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public boolean isAuthor(User user) {
        return author.equals(user);
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

    public ArticleTitle getTitle() {
        return title;
    }

    public Slug getSlug() {
        return slug;
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
        return createdAt.equals(article.createdAt) && title.equals(article.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, title);
    }
}
