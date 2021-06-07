package io.github.raeperd.realworld.domain.article;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.raeperd.realworld.domain.article.ArticleUpdateRequest.builder;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ArticleContentsTest {


    @Test
    void when_updateArticle_with_no_update_field_request_expect_not_changed() {
        final var articleContents = sampleArticleContents();
        final var emptyUpdateRequest = builder().build();

        articleContents.updateArticleContentsIfPresent(emptyUpdateRequest);

        assertThatEqualArticleContents(articleContents, sampleArticleContents());
    }

    @Test
    void when_updateArticle_with_all_field_expect_changed(@Mock ArticleTitle titleToUpdate) {
        final var articleContents = sampleArticleContents();
        final var fullUpdateRequest = builder().titleToUpdate(titleToUpdate)
                .descriptionToUpdate("descriptionToUpdate")
                .bodyToUpdate("bodyToUpdate")
                .build();

        articleContents.updateArticleContentsIfPresent(fullUpdateRequest);

        assertThat(articleContents.getTitle()).isEqualTo(titleToUpdate);
        assertThat(articleContents.getDescription()).isEqualTo("descriptionToUpdate");
        assertThat(articleContents.getBody()).isEqualTo("bodyToUpdate");
    }

    private ArticleContents sampleArticleContents() {
        return new ArticleContents("description", ArticleTitle.of("title"), "body", emptySet());
    }

    private void assertThatEqualArticleContents(ArticleContents left, ArticleContents right) {
        assertThat(equalsArticleContents(left, right)).isTrue();
    }

    private boolean equalsArticleContents(ArticleContents left, ArticleContents right) {
        if (!left.getTitle().equals(right.getTitle())) {
            return false;
        }
        if (!left.getDescription().equals(right.getDescription())) {
            return false;
        }
        if (!left.getBody().equals(right.getBody())) {
            return false;
        }
        return left.getTags().equals(right.getTags());
    }

}