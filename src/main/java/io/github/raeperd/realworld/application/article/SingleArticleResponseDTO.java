package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.Tag;
import io.github.raeperd.realworld.domain.user.profile.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.time.ZoneId.of;

@Getter
@RequiredArgsConstructor
public class SingleArticleResponseDTO {

    private final ArticleResponseDTO article;

    public static SingleArticleResponseDTO fromArticleAndProfile(Article article, Profile profile) {
        return new SingleArticleResponseDTO(ArticleResponseDTO.fromArticleAndProfile(article, profile));
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    static class ArticleResponseDTO {
        private final String slug;
        private final String title;
        private final String description;
        private final String body;
        private final Set<Tag> tagList;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;
        // TODO: Create ProfileDTO for unwrapped Profile view
        private final Profile author;
        private final boolean favorited;
        private final int favoritesCount;

        public static ArticleResponseDTO fromArticleAndProfile(Article article, Profile profile) {
            return ArticleResponseDTO.builder()
                    .slug(article.getTitle())
                    .title(article.getTitle())
                    .description(article.getDescription())
                    .body(article.getBody())
                    .tagList(new HashSet<>(article.getTagList()))
                    .createdAt(article.getCreatedAt().atZone(of("Asia/Seoul")))
                    .updatedAt(article.getUpdatedAt().atZone(of("Asia/Seoul")))
                    .author(profile)
                    .build();
        }
    }

}
