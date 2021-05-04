package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, Long> {

    boolean existsByUserAndArticle(User user, Article article);

    Optional<ArticleFavorite> findFirstByUserAndArticle(User user, Article article);

    long countAllByArticle(Article article);

    void deleteAllByArticle(Article article);

}
