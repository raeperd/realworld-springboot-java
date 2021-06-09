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

    @Test
    void when_articleTitle_has_different_slug_expect_not_equal_and_hashCode() {
        var articleTitle = ArticleTitle.of("some title");
        var articleTitleWithDifferentSlug = ArticleTitle.of("other Title");

        assertThat(articleTitleWithDifferentSlug)
                .isNotEqualTo(articleTitle)
                .extracting(ArticleTitle::hashCode)
                .isNotEqualTo(articleTitle.hashCode());
    }

    @Test
    void when_articleTitle_has_same_slug_expect_equal_and_hashCode() {
        var articleTitle = ArticleTitle.of("some title");
        var articleTitleWithSameSlug = ArticleTitle.of("Some Title");

        assertThat(articleTitleWithSameSlug)
                .isEqualTo(articleTitle)
                .hasSameHashCodeAs(articleTitle);
    }
}