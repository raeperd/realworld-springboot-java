package io.github.raeperd.realworld.application.article;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.tag.Tag;
import io.github.raeperd.realworld.domain.article.title.ArticleTitle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.util.stream.Collectors.toSet;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
@RequiredArgsConstructor
public class ArticlePostRequestDTO {

    private final String title;
    private final String description;
    private final String body;
    private final Set<String> tagList;

    public Article toArticle() {
        if (tagList == null) {
            return new Article(ArticleTitle.of(title), description, body);
        }
        return new Article(ArticleTitle.of(title), description, body, tagList.stream().map(Tag::new).collect(toSet()));
    }

}
