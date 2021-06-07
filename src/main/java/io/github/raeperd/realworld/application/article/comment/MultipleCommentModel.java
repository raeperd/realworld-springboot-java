package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.application.article.comment.CommentModel.CommentModelNested;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import lombok.Value;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Value
class MultipleCommentModel {

    List<CommentModelNested> comments;

    static MultipleCommentModel fromComments(Set<Comment> comments) {
        final var commentsCollected = comments.stream().map(CommentModelNested::fromComment)
                .collect(toList());
        return new MultipleCommentModel(commentsCollected);
    }
}
