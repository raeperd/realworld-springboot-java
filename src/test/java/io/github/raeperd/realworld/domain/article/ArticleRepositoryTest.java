package io.github.raeperd.realworld.domain.article;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.stream.Stream;

import static io.github.raeperd.realworld.domain.user.UserTestUtils.databaseUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnableJpaAuditing
@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository repository;

    @MethodSource("provideInvalidArticle")
    @ParameterizedTest
    void when_save_invalid_article_expect_DataIntegrityViolationException(Article invalidArticle) {
        assertThatThrownBy(() ->
                repository.save(invalidArticle)
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void when_save_article_expect_auditing_works() {
        var contentsToSave = new ArticleContents("description", ArticleTitle.of("some title"), "body");
        var articleToSave = databaseUser().writeArticle(contentsToSave);

        var articleSaved = repository.save(articleToSave);

        assertThat(articleSaved).hasNoNullFieldsOrProperties();
    }

    private static Stream<Arguments> provideInvalidArticle() {
        return provideInvalidArticleContents()
                .map(invalidArticleContents -> new Article(databaseUser(), invalidArticleContents))
                .map(Arguments::of);
    }

    private static Stream<ArticleContents> provideInvalidArticleContents() {
        return Stream.of(
                new ArticleContents(null, null, null),
                new ArticleContents("description", null,  null),
                new ArticleContents(null, ArticleTitle.of("title"),  null),
                new ArticleContents(null, null,  "body")
        );
    }

}