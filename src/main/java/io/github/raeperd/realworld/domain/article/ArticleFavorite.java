package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "article_id"}))
@Entity
class ArticleFavorite {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn(name = "article_id", nullable = false)
    @ManyToOne
    private Article article;

    protected ArticleFavorite() {
    }

    ArticleFavorite(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    Article getArticle() {
        return article;
    }

}
