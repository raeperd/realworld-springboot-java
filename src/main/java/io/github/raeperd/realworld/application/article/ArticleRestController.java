package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.domain.article.ArticleDeleteService;
import io.github.raeperd.realworld.domain.article.ArticleFavoriteService;
import io.github.raeperd.realworld.domain.article.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static io.github.raeperd.realworld.application.article.MultipleArticleResponseDTO.fromArticleViews;
import static io.github.raeperd.realworld.application.article.SingleArticleResponseDTO.fromArticleView;
import static java.util.stream.Collectors.toList;

@RequestMapping("/articles")
@RestController
public class ArticleRestController {

    private final ArticleService articleService;
    private final ArticleFavoriteService favoriteService;
    private final ArticleDeleteService deleteService;

    public ArticleRestController(ArticleService articleService, ArticleFavoriteService favoriteService, ArticleDeleteService deleteService) {
        this.articleService = articleService;
        this.favoriteService = favoriteService;
        this.deleteService = deleteService;
    }

    @PostMapping
    public SingleArticleResponseDTO postArticle(@RequestBody ArticlePostRequestDTO articlePostRequestDTO) {
        final var articleView = articleService.createAndViewArticle(articlePostRequestDTO.toArticle());
        return fromArticleView(articleView);
    }

    @PostMapping("/{slug}/favorite")
    public SingleArticleResponseDTO favoriteArticle(@PathVariable String slug) {
        return articleService.findArticleBySlug(slug)
                .map(favoriteService::favoriteArticleAndView)
                .map(SingleArticleResponseDTO::fromArticleView)
                .orElseThrow(NoSuchElementException::new);
    }

    @GetMapping
    public MultipleArticleResponseDTO getArticles(Pageable pageable) {
        final var articleViews = articleService.viewAllArticle(pageable)
                .stream().collect(toList());
        return fromArticleViews(articleViews);
    }

    @GetMapping("/{slug}")
    public SingleArticleResponseDTO getArticleBySlug(@PathVariable String slug) {
        final var article = articleService.viewArticleBySlug(slug).orElseThrow(NoSuchElementException::new);
        return fromArticleView(article);
    }

    @PutMapping("/{slug}")
    public SingleArticleResponseDTO putArticleBySlug(@PathVariable String slug, @RequestBody ArticlePutRequestDTO articlePutRequestDTO) {
        final var articleUpdated = articleService.updateArticleAndView(slug, articlePutRequestDTO.toUpdateCommand());
        return fromArticleView(articleUpdated);
    }

    @DeleteMapping("/{slug}")
    public void deleteArticleBySlug(@PathVariable String slug) {
        deleteService.deleteArticleBySlug(slug);
    }

}
