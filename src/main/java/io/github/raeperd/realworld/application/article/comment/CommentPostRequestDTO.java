package io.github.raeperd.realworld.application.article.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("comment")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
class CommentPostRequestDTO {

    @NotBlank
    private final String body;

    @JsonCreator
    CommentPostRequestDTO(String body) {
        this.body = body;
    }
}
