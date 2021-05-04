package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleFavoriteService {

    private final UserContextHolder userContextHolder;
    private final ArticleFavoriteRepository favoriteRepository;
    private final ArticleViewer articleViewer;

    public ArticleFavoriteService(UserContextHolder userContextHolder, ArticleFavoriteRepository favoriteRepository, ArticleViewer articleViewer) {
        this.userContextHolder = userContextHolder;
        this.favoriteRepository = favoriteRepository;
        this.articleViewer = articleViewer;
    }

    @Transactional
    public ArticleView favoriteArticleAndView(Article article) {
        final var currentUser = userContextHolder.getCurrentUser().orElseThrow(IllegalStateException::new);
        final var articleFavorite = favoriteRepository.save(new ArticleFavorite(currentUser, article));
        return articleViewer.viewArticleFromUser(articleFavorite.getArticle(), currentUser);
    }
}
