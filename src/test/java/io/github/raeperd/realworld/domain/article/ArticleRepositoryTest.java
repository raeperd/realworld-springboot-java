package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.tag.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void when_save_same_tag_from_another_article_expect_saved_successfully() {
        final var tagToSave = new Tag("some-tag");
        final var article = new Article("title", "description", "body", Set.of(tagToSave, new Tag("other-tag")));
        final var articleWithSameTag = new Article("title", "description", "body", Set.of(tagToSave));

        articleRepository.save(article);

        assertThat(articleRepository.save(articleWithSameTag).getTagList()).contains(tagToSave);
    }

}