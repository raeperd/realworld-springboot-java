package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.application.article.ArticleModel.ArticleModelNested;
import io.github.raeperd.realworld.domain.article.Article;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
class MultipleArticleModel {

    List<ArticleModelNested> articles;

    static MultipleArticleModel fromArticles(Page<Article> articles) {
        final var articlesCollected = articles.map(ArticleModelNested::fromArticle)
                .stream().collect(toList());
        return new MultipleArticleModel(articlesCollected);
    }
}
