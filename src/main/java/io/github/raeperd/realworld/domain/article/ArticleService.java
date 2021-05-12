package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.title.ArticleTitle;
import io.github.raeperd.realworld.domain.user.UserContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ArticleService {

    private final UserContextHolder userContextHolder;
    private final ArticleRepository articleRepository;
    private final ArticleViewer articleViewer;

    public ArticleService(UserContextHolder userContextHolder, ArticleRepository articleRepository, ArticleViewer articleViewer) {
        this.userContextHolder = userContextHolder;
        this.articleRepository = articleRepository;
        this.articleViewer = articleViewer;
    }

    @Transactional
    public ArticleView createAndViewArticle(Article article) {
        final var savedArticle = articleRepository.save(article);
        return viewArticleFromCurrentUser(savedArticle);
    }

    @Transactional(readOnly = true)
    public Page<ArticleView> viewAllArticle(Pageable pageable) {
        final var articles = articleRepository.findAll(pageable);
        return userContextHolder.getCurrentUser()
                .map(currentUser -> articles.map(this::viewArticleFromCurrentUser))
                .orElseGet(() -> articles.map(articleViewer::viewArticle));
    }

    @Transactional(readOnly = true)
    public Page<ArticleView> viewFeedFromCurrentUser(Pageable pageable) {
        final var currentUser = userContextHolder.getCurrentUser()
                .orElseThrow(IllegalStateException::new);
        return articleRepository.findAllByAuthorIn(currentUser.getFollowingUsers(), pageable)
                .map(article -> articleViewer.viewArticleFromUser(article, currentUser));
    }

    @Transactional(readOnly = true)
    public Optional<Article> findArticleBySlug(String slug) {
        return articleRepository.findFirstBySlug(ArticleTitle.of(slug).toSlug());
    }

    @Transactional
    public Optional<ArticleView> viewArticleBySlug(String slug) {
        return findArticleBySlug(slug)
                .map(this::viewArticleFromCurrentUser);
    }

    @Transactional
    public ArticleView updateArticleAndView(String slug, ArticleUpdateCommand articleUpdateCommand) {
        return articleRepository.findFirstBySlug(ArticleTitle.of(slug).toSlug())
                .map(article -> article.updateArticle(articleUpdateCommand))
                .map(this::viewArticleFromCurrentUser)
                .orElseThrow(NoSuchElementException::new);
    }

    private ArticleView viewArticleFromCurrentUser(Article article) {
        return userContextHolder.getCurrentUser()
                .map(currentUser -> articleViewer.viewArticleFromUser(article, currentUser))
                .orElseThrow(IllegalStateException::new);
    }

}
