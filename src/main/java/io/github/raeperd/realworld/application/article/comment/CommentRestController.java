package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.infrastructure.jwt.UserJWTPayload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class CommentRestController {

    private final CommentService commentService;

    CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{slug}/comments")
    public CommentModel postComments(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                     @PathVariable String slug, @Valid @RequestBody CommentPostRequestDTO dto) {
        final var commentAdded = commentService.createComment(jwtPayload.getUserId(), slug, dto.getBody());
        return CommentModel.fromComment(commentAdded);
    }
}
