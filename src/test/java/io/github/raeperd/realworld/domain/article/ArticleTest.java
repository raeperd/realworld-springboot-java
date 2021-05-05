package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.comment.Comment;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleTest {

    @Test
    void when_update_all_possible_field_expect_return_article_updated() {
        final var article = new Article("some title", null, null);
        final var updateCommand = ArticleUpdateCommand.builder()
                .title("title-updated").description("description-updated").body("body-updated")
                .build();

        assertThat(article.updateArticle(updateCommand))
                .hasFieldOrPropertyWithValue("title", "title-updated")
                .hasFieldOrPropertyWithValue("description", "description-updated")
                .hasFieldOrPropertyWithValue("body", "body-updated");
    }

    @Test
    void when_article_has_same_id_and_title_expect_equals() {
        final var article = new Article("some title", null, null);
        final var articleWithSameTitle = new Article(article.getTitle(), null, null);

        assertThat(article)
                .isEqualTo(articleWithSameTitle)
                .hasSameHashCodeAs(articleWithSameTitle);
    }

    @Test
    void when_create_article_with_empty_space_title_expect_dash_separated_slug() {
        final var article = new Article("some title", null, null);

        assertThat(article.getSlug()).isEqualTo("some-title");
    }

    @Test
    void when_delete_comment_by_id_with_another_user_expect_return_false(@Mock Comment comment, @Mock User user) {
        when(comment.getId()).thenReturn(0L);
        when(comment.isAuthor(user)).thenReturn(false);
        final var article = new Article("some title", null, null);
        article.addComment(comment);

        assertThat(article.deleteCommentByIdAndUser(0L, user)).isFalse();
    }

}