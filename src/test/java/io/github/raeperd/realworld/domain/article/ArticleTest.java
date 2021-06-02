package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleTest {

    @Mock
    private ArticleContents contents;
    @Mock
    private ArticleTitle title;
    @Mock
    private User author;

    @Test
    void when_article_has_different_author_expect_not_equal_and_hashCode(@Mock User otherUser) {
        when(contents.getTitle()).thenReturn(title);

        var article = new Article(author, contents);
        var articleFromOtherUser = new Article(otherUser, contents);

        assertThat(articleFromOtherUser)
                .isNotEqualTo(article)
                .extracting(Article::hashCode)
                .isNotEqualTo(article.hashCode());
    }

    @Test
    void when_article_has_different_contents_expect_not_equal_and_hashCode(@Mock ArticleContents otherContents, @Mock ArticleTitle otherTitle) {
        when(contents.getTitle()).thenReturn(title);
        when(otherContents.getTitle()).thenReturn(otherTitle);

        var article = new Article(author, contents);
        var articleWithOtherContents = new Article(author, otherContents);

        assertThat(articleWithOtherContents)
                .isNotEqualTo(article)
                .extracting(Article::hashCode)
                .isNotEqualTo(article.hashCode());
    }

    @Test
    void when_article_has_same_author_and_title_expect_equal_and_hashCode() {
        when(contents.getTitle()).thenReturn(title);

        var article = new Article(author, contents);
        var articleWithSameAuthorSlug = new Article(author, contents);

        assertThat(articleWithSameAuthorSlug)
                .isEqualTo(article)
                .hasSameHashCodeAs(article);
    }
}