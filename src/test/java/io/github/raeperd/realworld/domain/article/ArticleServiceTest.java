package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableJpaAuditing
@DataJpaTest
class ArticleServiceTest {

    @Autowired
    private ArticleRepository repository;
    @MockBean
    private AuditorAware<User> articleAuthorProvider;

    private ArticleService articleService;

    @BeforeEach
    void initializeService() {
        articleService = new ArticleService(repository);
    }

    @Test
    void when_save_article_expect_author_from_auditor() {
        given(articleAuthorProvider.getCurrentAuditor()).willReturn(empty());
        final var article = new Article("title", "description", "body", emptySet());

        articleService.createArticle(article);

        then(articleAuthorProvider).should(times(1)).getCurrentAuditor();
    }

    @Test
    void when_save_same_tag_from_another_article_expect_saved_successfully() {
        when(articleAuthorProvider.getCurrentAuditor()).thenReturn(empty());
        final var tagToSave = new Tag("some-tag");
        final var article = new Article("title", "description", "body", Set.of(tagToSave, new Tag("other-tag")));
        final var articleWithSameTag = new Article("title", "description", "body", Set.of(tagToSave));

        articleService.createArticle(article);

        assertThat(articleService.createArticle(articleWithSameTag).getTagList())
                .contains(tagToSave);
    }
}