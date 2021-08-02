package io.github.raeperd.realworld.domain.article.comment;

import io.github.raeperd.realworld.domain.article.ArticleFindService;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserFindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.springframework.data.util.Optionals.mapIfAllPresent;

@Service
public class CommentService {

    private final UserFindService userFindService;
    private final ArticleFindService articleFindService;

    CommentService(UserFindService userFindService, ArticleFindService articleFindService) {
        this.userFindService = userFindService;
        this.articleFindService = articleFindService;
    }

    @Transactional
    public Comment createComment(long userId, String slug, String body) {
        return mapIfAllPresent(userFindService.findById(userId), articleFindService.getArticleBySlug(slug),
                (user, article) -> user.writeCommentToArticle(article, body))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Set<Comment> getComments(long userId, String slug) {
        return mapIfAllPresent(userFindService.findById(userId), articleFindService.getArticleBySlug(slug),
                User::viewArticleComments)
                .orElseThrow(NoSuchElementException::new);
    }

    public Optional<Comment> getCommentById(Long userId, String slug, Long commentId) {
        return getComments(userId,slug)
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst();
    }

    @Transactional
    public void deleteCommentById(long userId, String slug, long commentId) {
        final var articleContainsComments = articleFindService.getArticleBySlug(slug)
                .orElseThrow(NoSuchElementException::new);
        userFindService.findById(userId)
                .ifPresentOrElse(user -> user.deleteArticleComment(articleContainsComments, commentId),
                        () -> {throw new NoSuchElementException();});
    }
}
