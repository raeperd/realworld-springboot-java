package io.github.raeperd.realworld.application.article;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.application.user.ProfileModel.ProfileModelNested;
import io.github.raeperd.realworld.domain.article.Article;
import lombok.Value;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.util.Collections.emptyList;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
class ArticleModel {

    String slug;
    String title;
    String description;
    String body;
    List<String> tagList;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;
    boolean favorited;
    int favoritesCount;
    ProfileModelNested author;

    // TODO: apply tags
    // TODO: apply favorited, favoritesCount
    static ArticleModel fromArticle(Article article) {
        final var contents = article.getContents();
        final var titleFromArticle = contents.getTitle();
        return new ArticleModel(
                titleFromArticle.getSlug(), titleFromArticle.getTitle(),
                contents.getDescription(), contents.getBody(),
                emptyList(),
                article.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")),
                article.getUpdatedAt().atZone(ZoneId.of("Asia/Seoul")),
                false, 0,
                ProfileModelNested.fromProfile(article.getAuthor().getProfile())
        );
    }
}
