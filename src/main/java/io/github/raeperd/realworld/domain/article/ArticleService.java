package io.github.raeperd.realworld.domain.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;

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
    public Optional<ArticleView> viewArticleBySlug(String slug) {
        return articleRepository.findFirstByTitle(slug)
                .map(articleViewer::viewArticle);
    }

    @Transactional
    public ArticleView updateArticleAndView(String slug, ArticleUpdateCommand articleUpdateCommand) {
        return articleRepository.findFirstByTitle(slug)
                .map(article -> article.updateArticle(articleUpdateCommand))
                .map(articleViewer::viewArticle)
                .orElseThrow(NoSuchElementException::new);
    }

    // TODO Can be improved
    @Transactional
    public void deleteArticleBySlug(Long authorId, String slug) {
        final var articleToDelete = articleRepository.findFirstByTitle(slug)
                .orElseThrow(NoSuchElementException::new);
        if (!articleToDelete.getAuthor().getId().equals(authorId)) {
            throw new IllegalAccessError(format("User with id(%d) is not authorized to delete Article(%s)",
                    authorId, articleToDelete.getTitle()));
        }
        articleRepository.delete(articleToDelete);
    }
}
