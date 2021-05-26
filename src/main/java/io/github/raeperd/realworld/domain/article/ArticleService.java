package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Article createNewArticle(User author, ArticleContents contents) {
        return articleRepository.save(author.writeArticle(contents));
    }
}
