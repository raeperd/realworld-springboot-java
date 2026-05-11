package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.application.article.comment.CommentModel.CommentModelNested;
import io.github.raeperd.realworld.domain.article.comment.Comment;

import java.util.List;
import java.util.Set;

record MultipleCommentModel(List<CommentModelNested> comments) {

    static MultipleCommentModel fromComments(Set<Comment> comments) {
        final var commentsCollected = comments.stream().map(CommentModelNested::fromComment)
                .toList();
        return new MultipleCommentModel(commentsCollected);
    }
}
