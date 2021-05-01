package io.github.raeperd.realworld.application.article;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
@RequiredArgsConstructor
public class ArticlePostRequestDTO {

    private final String title;
    private final String description;
    private final String body;
    private final Set<Tag> tagList;

    public Article toArticle() {
        if (tagList == null) {
            return new Article(title, description, body);
        }
        return new Article(title, description, body, tagList);
    }

}
