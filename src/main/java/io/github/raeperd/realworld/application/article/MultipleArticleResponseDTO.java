package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.application.article.SingleArticleResponseDTO.ArticleResponseDTO;
import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.user.profile.Profile;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Getter
public class MultipleArticleResponseDTO {

    private final List<ArticleResponseDTO> articles;
    private final int articlesCount;

    public MultipleArticleResponseDTO(List<ArticleResponseDTO> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }

    public static MultipleArticleResponseDTO fromArticleProfileMap(Map<Article, Profile> articleProfileMap) {
        final var articleResponseDTOs = articleProfileMap.entrySet().stream()
                .map(entry -> ArticleResponseDTO.fromArticleAndProfile(entry.getKey(), entry.getValue()))
                .collect(toList());
        return new MultipleArticleResponseDTO(articleResponseDTOs);
    }
}
