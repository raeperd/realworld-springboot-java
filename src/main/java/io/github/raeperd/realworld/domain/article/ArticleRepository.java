package io.github.raeperd.realworld.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ArticleRepository extends JpaRepository<Article, Long> {
}
