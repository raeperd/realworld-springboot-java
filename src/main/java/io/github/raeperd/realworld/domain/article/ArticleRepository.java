package io.github.raeperd.realworld.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findFirstBySlug(String title);
}
