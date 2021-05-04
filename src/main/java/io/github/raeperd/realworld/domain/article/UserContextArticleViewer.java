package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserContextHolder;
import org.springframework.stereotype.Component;

@Component
class UserContextArticleViewer implements ArticleViewer {

    private final UserContextHolder userContextHolder;
    private final ArticleFavoriteRepository favoriteRepository;

    public UserContextArticleViewer(UserContextHolder userContextHolder, ArticleFavoriteRepository favoriteRepository) {
        this.userContextHolder = userContextHolder;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public ArticleView viewArticle(Article article) {
        final var currentUser = userContextHolder.getCurrentUser().orElseThrow(IllegalStateException::new);
        return viewArticleFromUser(article, currentUser);
    }

    @Override
    public ArticleView viewArticleFromUser(Article article, User fromUser) {
        return ArticleView.of(article,
                fromUser.viewProfile(article.getAuthor()),
                favoriteRepository.existsByUserAndArticle(fromUser, article),
                favoriteRepository.countAllByArticle(article));
    }

}
