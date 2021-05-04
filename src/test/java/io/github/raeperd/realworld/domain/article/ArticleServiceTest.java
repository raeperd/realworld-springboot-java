package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
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
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleViewer articleViewer;

    private ArticleService articleService;

    @BeforeEach
    void initializeService() {
        articleService = new ArticleService(articleRepository, articleViewer);
    }

    @Test
    void when_delete_by_not_exists_slug_expect_NoSuchElementException() {
        when(articleRepository.findFirstByTitle(anyString())).thenReturn(empty());

        assertThatThrownBy(() ->
                articleService.deleteArticleBySlug(1L, "slug")
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void when_delete_by_slug_from_reader_expect_IllegalAccessError(@Mock Article article, @Mock User author) {
        when(articleRepository.findFirstByTitle(anyString())).thenReturn(of(article));
        when(article.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(-1L);

        assertThatThrownBy(() ->
                articleService.deleteArticleBySlug(1L, "some-slug")
        ).isInstanceOf(IllegalAccessError.class);
    }

    @Test
    void when_delete_by_slug_from_author_expect_to_delete(@Mock User currentUser, @Mock Article article) {
        given(articleRepository.findFirstByTitle(anyString())).willReturn(of(article));
        given(article.getAuthor()).willReturn(currentUser);
        given(currentUser.getId()).willReturn(1L);

        articleService.deleteArticleBySlug(1L, "some-slug");

        then(articleRepository).should(times(1)).delete(article);
    }
}