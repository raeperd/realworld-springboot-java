package io.github.raeperd.realworld.application.article.comment;


import io.github.raeperd.realworld.domain.article.comment.CommentService;
import org.springframework.web.bind.annotation.*;

import static io.github.raeperd.realworld.application.article.comment.SingleCommentResponseDTO.fromCommentView;

@RequestMapping("/articles")
@RestController
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{slug}/comments")
    public SingleCommentResponseDTO postComment(@RequestBody CommentPostRequestDTO requestDTO, @PathVariable String slug) {
        final var commentSaved = commentService.commentArticleBySlug(slug, requestDTO.toComment());
        return fromCommentView(commentService.viewCommentFromCurrentUser(commentSaved));
    }
}
