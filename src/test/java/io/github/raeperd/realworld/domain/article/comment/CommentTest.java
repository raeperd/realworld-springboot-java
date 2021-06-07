package io.github.raeperd.realworld.domain.article.comment;

import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.stream.Stream;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CommentTest {

    @MethodSource("provideDifferentComments")
    @ParameterizedTest
    void when_compare_different_comment_expect_not_equal(Comment commentLeft, Comment commentRight) {
        assertThat(commentLeft).isNotEqualTo(commentRight)
                .extracting(Comment::hashCode)
                .isNotEqualTo(commentRight.hashCode());
    }

    @Test
    void when_compare_same_comment_expect_equal_and_hashCode(@Mock Article article, @Mock User author) {
        var now = Instant.now();
        var commentLeft = commentWithCreatedAt(article, author, "body", now);
        var commentRight = commentWithCreatedAt(article, author, "body", now);

        assertThat(commentLeft)
                .isEqualTo(commentRight)
                .hasSameHashCodeAs(commentRight);
    }

    private static Stream<Arguments> provideDifferentComments() {
        var articleSample = mock(Article.class);
        var authorSample = mock(User.class);
        var bodySample = "bodySample";
        var createAtSample = now();
        var commentSample = commentWithCreatedAt(articleSample, authorSample, bodySample, createAtSample);
        return Stream.of(
                Pair.of(commentSample, commentWithCreatedAt(mock(Article.class), authorSample, bodySample, createAtSample)),
                Pair.of(commentSample, commentWithCreatedAt(articleSample, mock(User.class), bodySample, createAtSample)),
                Pair.of(commentSample, commentWithCreatedAt(articleSample, authorSample, "different body", createAtSample)),
                Pair.of(commentSample, commentWithCreatedAt(articleSample, authorSample, bodySample, now().plusSeconds(10))))
                .map(pair -> Arguments.of(pair.getFirst(), pair.getSecond()));
    }

    private static Comment commentWithCreatedAt(Article article, User author, String body, Instant createdAt) {
        var comment = new Comment(article, author, body);
        ReflectionTestUtils.setField(comment, "createdAt", createdAt);
        return comment;
    }

}