package io.github.raeperd.realworld.domain.article;

import org.springframework.data.repository.Repository;

interface ArticleRepository extends Repository<Article, Long> {

    Article save(Article article);

}
