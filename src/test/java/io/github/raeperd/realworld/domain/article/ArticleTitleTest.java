package io.github.raeperd.realworld.domain.article;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTitleTest {

    @Test
    void when_create_with_title_expect_valid_slug() {
        var title = "\n\n Some?\t\n Title  .";
        var slugExpected = "some-title";

        var articleTitle = ArticleTitle.of(title);

        assertThat(articleTitle.getSlug()).isEqualTo(slugExpected);
    }
}