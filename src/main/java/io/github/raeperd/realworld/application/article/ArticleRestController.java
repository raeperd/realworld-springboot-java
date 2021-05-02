package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.domain.article.ArticleService;
import io.github.raeperd.realworld.domain.user.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static io.github.raeperd.realworld.application.article.ArticleResponseDTO.fromArticleAndProfile;

@RequestMapping("/articles")
@RestController
public class ArticleRestController {

    private final ArticleService articleService;
    private final ProfileService profileService;

    public ArticleRestController(ArticleService articleService, ProfileService profileService) {
        this.articleService = articleService;
        this.profileService = profileService;
    }

    @PostMapping
    public ArticleResponseDTO postArticle(@RequestBody ArticlePostRequestDTO articlePostRequestDTO) {
        final var article = articleService.createArticle(articlePostRequestDTO.toArticle());
        final var authorProfile = profileService.viewProfileByUsername(article.getAuthor().getUsername());
        return fromArticleAndProfile(article, authorProfile);
    }

    @GetMapping("/{slug}")
    public ArticleResponseDTO getArticleBySlug(@PathVariable String slug) {
        final var article = articleService.findArticleBySlug(slug).orElseThrow(NoSuchElementException::new);
        final var authorProfile = profileService.viewProfileByUsername(article.getAuthor().getUsername());
        return fromArticleAndProfile(article, authorProfile);
    }

    @PutMapping("/{slug}")
    public ArticleResponseDTO putArticleBySlug(@PathVariable String slug, @RequestBody ArticlePutRequestDTO articlePutRequestDTO) {
        final var articleUpdated = articleService.updateArticle(slug, articlePutRequestDTO.toUpdateCommand());
        final var authorProfile = profileService.viewProfileByUsername(articleUpdated.getAuthor().getUsername());
        return fromArticleAndProfile(articleUpdated, authorProfile);
    }

    @DeleteMapping("/{slug}")
    public void deleteArticleBySlug(@PathVariable String slug) {
        articleService.deleteArticleBySlug(slug);
    }

}
