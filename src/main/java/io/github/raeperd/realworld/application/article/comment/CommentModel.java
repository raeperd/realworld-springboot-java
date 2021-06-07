package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.application.user.ProfileModel.ProfileModelNested;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import lombok.Value;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Value
class CommentModel {

    CommentModelNested comment;

    static CommentModel fromComment(Comment comment) {
        return new CommentModel(CommentModelNested.fromComment(comment));
    }

    @Value
    static class CommentModelNested {
        long id;
        String body;
        ZonedDateTime createdAt;
        ZonedDateTime updatedAt;
        ProfileModelNested author;

        static CommentModelNested fromComment(Comment comment) {
            return new CommentModelNested(comment.getId(),
                    comment.getBody(),
                    comment.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")),
                    comment.getUpdatedAt().atZone(ZoneId.of("Asia/Seoul")),
                    ProfileModelNested.fromProfile(comment.getAuthor().getProfile()));
        }
    }
}
