package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;

interface ArticleViewer {

    ArticleView viewArticle(Article article);

    ArticleView viewArticleFromUser(Article article, User fromUser);

}
