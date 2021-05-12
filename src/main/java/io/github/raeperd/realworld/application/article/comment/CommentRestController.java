package io.github.raeperd.realworld.application.article.comment;


import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.domain.article.title.Slug;
import org.springframework.web.bind.annotation.*;

import static io.github.raeperd.realworld.application.article.comment.MultipleCommentResponseDTO.fromCommentViews;
import static io.github.raeperd.realworld.application.article.comment.SingleCommentResponseDTO.fromCommentView;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequestMapping("/articles")
@RestController
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{slug}/comments")
    public SingleCommentResponseDTO postComment(@RequestBody CommentPostRequestDTO requestDTO, @PathVariable String slug) {
        final var commentSaved = commentService.commentArticleBySlug(Slug.fromString(slug), requestDTO.toComment());
        return fromCommentView(commentService.viewCommentFromCurrentUser(commentSaved));
    }

    @GetMapping("/{slug}/comments")
    public MultipleCommentResponseDTO getComments(@PathVariable String slug) {
        return fromCommentViews(
                commentService.viewAllCommentsBySlugFromCurrentUser(Slug.fromString(slug)));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{slug}/comments/{id}")
    public void deleteCommentByID(@PathVariable String slug, @PathVariable long id) {
        commentService.deleteCommentInArticleById(Slug.fromString(slug), id);
    }
}
