package io.github.raeperd.realworld.domain.article.comment;

import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = EAGER)
    private Article article;

    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = EAGER)
    private User author;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "body", nullable = false)
    private String body;

    public Comment(Article article, User author, String body) {
        this.article = article;
        this.author = author;
        this.body = body;
    }

    protected Comment() {
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var comment = (Comment) o;
        return article.equals(comment.article) && author.equals(comment.author) && Objects.equals(createdAt, comment.createdAt) && body.equals(comment.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(article, author, createdAt, body);
    }
}
