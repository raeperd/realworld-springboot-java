package io.github.raeperd.realworld.application.article;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
@Builder
@RequiredArgsConstructor
public class ArticleResponseDTO {

    private final String slug;
    private final String title;
    private final String description;
    private final String body;
    private final Set<Tag> tagList;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final boolean favorited;
    private final int favoritesCount;

    public static ArticleResponseDTO fromArticle(Article article) {
        return ArticleResponseDTO.builder()
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(new HashSet<>(article.getTagList()))
                .build();
    }

}
