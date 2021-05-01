package io.github.raeperd.realworld.application.article;

import io.github.raeperd.realworld.domain.article.ArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/articles")
@RestController
public class ArticleRestController {

    private final ArticleService articleService;

    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ArticleResponseDTO postArticle(@RequestBody ArticlePostRequestDTO articlePostRequestDTO) {
        final var article = articlePostRequestDTO.toArticle();
        return ArticleResponseDTO.fromArticle(
                articleService.createArticle(article));
    }
}
