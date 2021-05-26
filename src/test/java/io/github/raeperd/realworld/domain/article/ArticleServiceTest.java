package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    private ArticleService articleService;

    @Mock
    private ArticleRepository repository;
    @Mock
    private User author;
    @Mock
    private ArticleContents contents;

    @BeforeEach
    private void initializeService() {
        articleService = new ArticleService(repository);
    }

    @Test
    void when_createNewArticle_then_author_writeArticle_contents() {
        articleService.createNewArticle(author, contents);

        then(author).should(times(1)).writeArticle(contents);
    }

    @Test
    void given_author_writeArticle_then_userRepository_save(@Mock Article article) {
        given(author.writeArticle(contents)).willReturn(article);

        articleService.createNewArticle(author, contents);

        then(repository).should(times(1)).save(article);
    }

}