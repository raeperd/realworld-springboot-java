package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.domain.article.ArticleService;
import io.github.raeperd.realworld.domain.jwt.JWTPayload;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static io.github.raeperd.realworld.application.article.MultipleArticleResponseDTO.fromArticleViews;
import static io.github.raeperd.realworld.application.article.SingleArticleResponseDTO.fromArticleView;
import static java.util.stream.Collectors.toList;

@RequestMapping("/articles")
@RestController
public class ArticleRestController {

    private final ArticleService articleService;

    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public SingleArticleResponseDTO postArticle(@RequestBody ArticlePostRequestDTO articlePostRequestDTO) {
        final var articleView = articleService.createAndViewArticle(articlePostRequestDTO.toArticle());
        return fromArticleView(articleView);
    }

    @PostMapping("/{slug}/favorited")
    public SingleArticleResponseDTO favortieArticle() {
        return null;
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
    public void deleteArticleBySlug(@AuthenticationPrincipal JWTPayload jwtPayload, @PathVariable String slug) {
        articleService.deleteArticleBySlug(jwtPayload.getUserId(), slug);
    }

}
