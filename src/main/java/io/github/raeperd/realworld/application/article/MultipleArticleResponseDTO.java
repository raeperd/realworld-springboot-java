package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.application.article.SingleArticleResponseDTO.ArticleResponseDTO;
import io.github.raeperd.realworld.domain.article.ArticleView;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class MultipleArticleResponseDTO {

    private final List<ArticleResponseDTO> articles;
    private final int articlesCount;

    private MultipleArticleResponseDTO(List<ArticleResponseDTO> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }

    public static MultipleArticleResponseDTO fromArticleViews(Collection<ArticleView> articleViews) {
        return new MultipleArticleResponseDTO(articleViews.stream()
                .map(ArticleResponseDTO::fromArticleView)
                .collect(toList()));
    }
}
