package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.title.Slug;
import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findFirstBySlug(Slug slug);

    Page<Article> findAllByAuthorIn(Collection<User> users, Pageable pageable);

}
