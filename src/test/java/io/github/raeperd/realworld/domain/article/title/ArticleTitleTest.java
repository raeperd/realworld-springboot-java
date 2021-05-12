package io.github.raeperd.realworld.domain.article.title;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTitleTest {

    @Test
    void when_to_slug_expect_dash_separated_slug() {
        final var title = ArticleTitle.of("Some Title Of Article");
        final var slugExpected = "some-title-of-article";

        assertThat(title.toSlug()).hasToString(slugExpected);
    }

    @Test
    void when_title_with_same_string_expect_equal_and_hashcode() {
        final var left = ArticleTitle.of("some-title");
        final var right = ArticleTitle.of("some-title");

        assertThat(left)
                .isEqualTo(right)
                .hasSameHashCodeAs(right);
    }

}