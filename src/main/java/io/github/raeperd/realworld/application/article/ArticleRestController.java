package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.domain.article.ArticleService;
import io.github.raeperd.realworld.domain.user.profile.ProfileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
