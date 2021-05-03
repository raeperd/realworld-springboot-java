package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.UserContextHolder;
import io.github.raeperd.realworld.domain.user.profile.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.function.Function.identity;

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

    @Transactional(readOnly = true)
    public Map<Article, Profile> getAllArticles(Pageable pageable) {
        final var currentUser = userContextHolder.getCurrentUser()
                .orElseThrow(IllegalStateException::new);
        return articleRepository.findAll(pageable).stream()
                .collect(Collectors.toMap(identity(), article -> currentUser.viewProfile(article.getAuthor())));
    }

    @Transactional
    public Optional<Article> findArticleBySlug(String slug) {
        return articleRepository.findFirstByTitle(slug);
    }

    @Transactional
    public Article updateArticle(String slug, ArticleUpdateCommand articleUpdateCommand) {
        return articleRepository.findFirstByTitle(slug)
                .map(article -> article.updateArticle(articleUpdateCommand))
                .orElseThrow(NoSuchElementException::new);
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
