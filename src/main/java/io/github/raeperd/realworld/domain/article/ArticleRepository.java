package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findFirstBySlug(String title);

    Page<Article> findAllByAuthorIn(Collection<User> users, Pageable pageable);

}
