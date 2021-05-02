package io.github.raeperd.realworld.application.article;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.article.ArticleUpdateCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
@RequiredArgsConstructor
public class ArticlePutRequestDTO {

    private final String title;
    private final String description;
    private final String body;

    public ArticleUpdateCommand toUpdateCommand() {
        return ArticleUpdateCommand.builder()
                .title(title)
                .description(description)
                .body(body)
                .build();
    }

}
