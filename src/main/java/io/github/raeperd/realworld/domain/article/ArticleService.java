package io.github.raeperd.realworld.domain.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleViewer articleViewer;

    public ArticleService(ArticleRepository articleRepository, ArticleViewer articleViewer) {
        this.articleRepository = articleRepository;
        this.articleViewer = articleViewer;
    }

    @Transactional
    public ArticleView createAndViewArticle(Article article) {
        final var savedArticle = articleRepository.save(article);
        return articleViewer.viewArticle(savedArticle);
    }

    @Transactional(readOnly = true)
    public Page<ArticleView> viewAllArticle(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(articleViewer::viewArticle);
    }

    @Transactional
    public Optional<Article> findArticleBySlug(String slug) {
        return articleRepository.findFirstByTitle(slug);
    }

    @Transactional
    public Optional<ArticleView> viewArticleBySlug(String slug) {
        return findArticleBySlug(slug)
                .map(articleViewer::viewArticle);
    }

    @Transactional
    public ArticleView updateArticleAndView(String slug, ArticleUpdateCommand articleUpdateCommand) {
        return articleRepository.findFirstByTitle(slug)
                .map(article -> article.updateArticle(articleUpdateCommand))
                .map(articleViewer::viewArticle)
                .orElseThrow(NoSuchElementException::new);
    }

}
