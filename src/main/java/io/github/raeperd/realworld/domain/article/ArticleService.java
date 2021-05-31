package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.UserFindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ArticleService {

    private final UserFindService userFindService;
    private final ArticleRepository articleRepository;

    ArticleService(UserFindService userFindService, ArticleRepository articleRepository) {
        this.userFindService = userFindService;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Article createNewArticle(long authorId, ArticleContents contents) {
        return userFindService.findById(authorId)
                .map(author -> author.writeArticle(contents))
                .map(articleRepository::save)
                .orElseThrow(NoSuchElementException::new);
    }
}
