package io.github.raeperd.realworld.domain.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

interface ArticleRepository extends Repository<Article, Long> {

    Article save(Article article);
    Page<Article> findAll(Pageable pageable);
}
