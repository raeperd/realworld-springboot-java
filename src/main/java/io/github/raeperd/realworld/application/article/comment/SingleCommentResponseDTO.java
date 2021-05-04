package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.domain.article.comment.CommentView;
import io.github.raeperd.realworld.domain.user.profile.Profile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@RequiredArgsConstructor
public class SingleCommentResponseDTO {

    private final CommentResponseDTO comment;

    static SingleCommentResponseDTO fromCommentView(CommentView commentView) {
        return new SingleCommentResponseDTO(CommentResponseDTO.fromCommentView(commentView));
    }

    @Getter
    @RequiredArgsConstructor
    static class CommentResponseDTO {
        private final Long id;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;
        private final Profile author;
        private final String body;

        static CommentResponseDTO fromCommentView(CommentView commentView) {
            return new CommentResponseDTO(commentView.getId(),
                    commentView.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")),
                    commentView.getUpdatedAt().atZone(ZoneId.of("Asia/Seoul")),
                    commentView.getAuthorProfile(),
                    commentView.getBody()
            );
        }
    }
}
