package io.github.raeperd.realworld.domain.article;

import org.springframework.stereotype.Service;

@Service
public class UserContextArticleViewer implements ArticleViewer {

    @Override
    public ArticleView viewArticle(Article article) {
        return null;
    }

}
