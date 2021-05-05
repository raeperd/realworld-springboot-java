package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static java.lang.String.format;

@Service
public class ArticleDeleteService {

    private final UserContextHolder userContextHolder;
    private final ArticleRepository articleRepository;
    private final ArticleFavoriteRepository favoriteRepository;

    public ArticleDeleteService(UserContextHolder userContextHolder, ArticleRepository articleRepository, ArticleFavoriteRepository favoriteRepository) {
        this.userContextHolder = userContextHolder;
        this.articleRepository = articleRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public void deleteArticleBySlug(String slug) {
        final var currentUser = userContextHolder.getCurrentUser()
                .orElseThrow(IllegalStateException::new);
        final var articleToDelete = articleRepository.findFirstBySlug(slug)
                .orElseThrow(NoSuchElementException::new);
        if (!articleToDelete.isAuthor(currentUser)) {
            throw new IllegalAccessError(
                    format("User(%s) is not authorized to delete article(%s)", currentUser.getEmail(), articleToDelete.getTitle()));
        }
        deleteArticle(articleToDelete);
    }

    private void deleteArticle(Article article) {
        favoriteRepository.deleteAllByArticle(article);
        articleRepository.delete(article);
    }
}
