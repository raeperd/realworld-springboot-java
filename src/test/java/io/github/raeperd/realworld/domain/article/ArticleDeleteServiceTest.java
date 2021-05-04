package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleDeleteServiceTest {

    @Mock
    private UserContextHolder userContextHolder;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleFavoriteRepository favoriteRepository;

    private ArticleDeleteService deleteService;

    @BeforeEach
    void initializeService() {
        deleteService = new ArticleDeleteService(userContextHolder, articleRepository, favoriteRepository);
    }

    @Test
    void when_getCurrentUser_return_empty_expect_IllegalStateException() {
        when(userContextHolder.getCurrentUser()).thenReturn(empty());

        assertThatThrownBy(() ->
                deleteService.deleteArticleBySlug("slug-to-delete-without-user")
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void when_delete_by_not_exists_slug_expect_NoSuchElementException(@Mock User user) {
        when(userContextHolder.getCurrentUser()).thenReturn(of(user));
        when(articleRepository.findFirstBySlug(anyString())).thenReturn(empty());

        assertThatThrownBy(() ->
                deleteService.deleteArticleBySlug("slug")
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void when_delete_by_slug_from_not_author_expect_IllegalAccessError(@Mock Article article, @Mock User user) {
        when(userContextHolder.getCurrentUser()).thenReturn(of(user));
        when(articleRepository.findFirstBySlug(anyString())).thenReturn(of(article));
        when(article.isAuthor(user)).thenReturn(false);

        assertThatThrownBy(() ->
                deleteService.deleteArticleBySlug("some-slug")
        ).isInstanceOf(IllegalAccessError.class);
    }

    @Test
    void when_delete_by_slug_from_author_expect_to_delete(@Mock User currentUser, @Mock Article article) {
        given(userContextHolder.getCurrentUser()).willReturn(of(currentUser));
        given(articleRepository.findFirstBySlug(anyString())).willReturn(of(article));
        given(article.isAuthor(currentUser)).willReturn(true);

        deleteService.deleteArticleBySlug("some-slug");

        then(articleRepository).should(times(1)).delete(article);
    }
}