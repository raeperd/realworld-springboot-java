package io.github.raeperd.realworld.application.article.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("comment")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
public class CommentPostRequestDTO {

    private final String body;

    @JsonCreator
    public CommentPostRequestDTO(@JsonProperty("body") String body) {
        this.body = body;
    }

    public Comment toComment() {
        return new Comment(body);
    }

}
