package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.infrastructure.jwt.UserJWTPayload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/articles/{slug}/comments")
    public MultipleCommentModel getComments(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                            @PathVariable String slug) {
        final var comments = commentService.getComments(jwtPayload.getUserId(), slug);
        return MultipleCommentModel.fromComments(comments);
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    public void deleteComment(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                              @PathVariable String slug, @PathVariable long id) {
        commentService.deleteCommentById(jwtPayload.getUserId(), slug, id);
    }
}
