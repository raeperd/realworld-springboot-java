package io.github.raeperd.realworld.domain.article;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @Test
    void when_update_all_possible_field_expect_return_article_updated() {
        final var article = new Article(null, null, null);
        final var updateCommand = ArticleUpdateCommand.builder()
                .title("title-updated").description("description-updated").body("body-updated")
                .build();

        assertThat(article.updateArticle(updateCommand))
                .hasFieldOrPropertyWithValue("title", "title-updated")
                .hasFieldOrPropertyWithValue("description", "description-updated")
                .hasFieldOrPropertyWithValue("body", "body-updated");
    }
}