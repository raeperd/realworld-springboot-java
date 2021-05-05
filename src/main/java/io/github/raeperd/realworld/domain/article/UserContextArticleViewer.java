package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.profile.Profile;
import org.springframework.stereotype.Component;

@Component
class UserContextArticleViewer implements ArticleViewer {

    private final ArticleFavoriteRepository favoriteRepository;

    public UserContextArticleViewer(ArticleFavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public ArticleView viewArticle(Article article) {
        return ArticleView.of(article,
                Profile.fromUser(article.getAuthor()),
                false,
                favoriteRepository.countAllByArticle(article));
    }

    @Override
    public ArticleView viewArticleFromUser(Article article, User fromUser) {
        return ArticleView.of(article,
                fromUser.viewProfile(article.getAuthor()),
                favoriteRepository.existsByUserAndArticle(fromUser, article),
                favoriteRepository.countAllByArticle(article));
    }

}
