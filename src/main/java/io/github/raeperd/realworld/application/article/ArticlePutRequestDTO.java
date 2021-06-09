package io.github.raeperd.realworld.application.article;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.article.ArticleTitle;
import io.github.raeperd.realworld.domain.article.ArticleUpdateRequest;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static io.github.raeperd.realworld.domain.article.ArticleUpdateRequest.builder;
import static java.util.Optional.ofNullable;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
class ArticlePutRequestDTO {

    String title;
    String description;
    String body;

    ArticleUpdateRequest toUpdateRequest() {
        return builder().titleToUpdate(ofNullable(title).map(ArticleTitle::of).orElse(null))
                .descriptionToUpdate(description)
                .bodyToUpdate(body)
                .build();
    }
}
