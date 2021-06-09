package io.github.raeperd.realworld.domain.article.comment;

import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.ArticleFindService;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private CommentService commentService;

    @Mock
    private UserFindService userFindService;
    @Mock
    private ArticleFindService articleFindService;

    @BeforeEach
    private void initializeService() {
        commentService = new CommentService(userFindService, articleFindService);
    }

    @Test
    void when_articleFindService_return_empty_expect_NoSuchElementException() {
        when(articleFindService.getArticleBySlug("slug")).thenReturn(empty());

        assertThatThrownBy(() ->
                commentService.deleteCommentById(1L, "slug", 2L)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void when_userFindService_return_empty_expect_NoSuchElementException(@Mock Article article) {
        when(articleFindService.getArticleBySlug("slug")).thenReturn(of(article));
        when(userFindService.findById(1L)).thenReturn(empty());

        assertThatThrownBy(() ->
                commentService.deleteCommentById(1L, "slug", 2L)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void given_user_and_article_deleteCommentById_then_user_deleteArticleComment(@Mock User user, @Mock Article article) {
        given(userFindService.findById(1L)).willReturn(of(user));
        given(articleFindService.getArticleBySlug("slug")).willReturn(of(article));

        commentService.deleteCommentById(1L, "slug", 2L);

        then(user).should(times(1)).deleteArticleComment(article, 2L);
    }

}