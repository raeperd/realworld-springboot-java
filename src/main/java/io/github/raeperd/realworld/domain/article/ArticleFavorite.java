package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "article_id"}))
@Entity
public class ArticleFavorite {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    protected ArticleFavorite() {
    }

    public ArticleFavorite(User user, Article article) {
        this.user = user;
        this.article = article;
    }
}
