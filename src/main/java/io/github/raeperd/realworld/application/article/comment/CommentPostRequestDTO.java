package io.github.raeperd.realworld.application.article.comment;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("comment")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
record CommentPostRequestDTO(@NotBlank String body) {
}
