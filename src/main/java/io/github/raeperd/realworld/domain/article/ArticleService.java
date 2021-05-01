package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserContextHolder userContextHolder;

    public ArticleService(ArticleRepository articleRepository, UserContextHolder userContextHolder) {
        this.articleRepository = articleRepository;
        this.userContextHolder = userContextHolder;
    }

    @Transactional
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public Optional<Article> findArticleBySlug(String slug) {
        return articleRepository.findFirstByTitle(slug);
    }

    @Transactional
    public void deleteArticleBySlug(String slug) {
        final var currentUser = userContextHolder.getCurrentUser()
                .orElseThrow(IllegalStateException::new);
        final var articleToDelete = articleRepository.findFirstByTitle(slug)
                .orElseThrow(NoSuchElementException::new);
        if (!currentUser.equals(articleToDelete.getAuthor())) {
            throw new IllegalAccessError(format("User(%s) is not authorized to delete Article(%s)",
                    currentUser.getEmail(), articleToDelete.getTitle()));
        }
        articleRepository.delete(articleToDelete);
    }
}
