package io.github.raeperd.realworld.domain.article.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.lang.String.valueOf;
import static java.util.Collections.singleton;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private TagService tagService;
    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    void initializeService() {
        this.tagService = new TagService(tagRepository);
    }

    @Test
    void when_findAll_expect_repository_findAll_called() {
        tagService.findAll();

        then(tagRepository).should(times(1)).findAll();
    }

    @Test
    void given_tags_reloadAllTagsIfAlreadyPresent_then_findFirstByValue() {
        tagService.reloadAllTagsIfAlreadyPresent(singleton(new Tag("tag-to-search")));

        then(tagRepository).should(times(1)).findFirstByValue("tag-to-search");
    }

    @Test
    void when_repository_return_empty_expect_return_same_tags_expect_reloadAllTagsIfAlreadyPresent_return_same() {
        final var tagNotExists = new Tag("tag-not-exits");
        when(tagRepository.findFirstByValue(valueOf(tagNotExists))).thenReturn(empty());

        final var tagsReloaded = tagService.reloadAllTagsIfAlreadyPresent(singleton(tagNotExists));

        assertThat(tagsReloaded).contains(tagNotExists);
    }

    @Test
    void when_repository_find_already_exists_tags_expect_reloadAllTagsIfAlreadyPresent_return_from_repository(@Mock Tag tagAlreadyExists) {
        when(tagRepository.findFirstByValue("tag-already-exists")).thenReturn(of(tagAlreadyExists));

        final var tagsReloaded = tagService.reloadAllTagsIfAlreadyPresent(singleton(new Tag("tag-already-exists")));

        assertThat(tagsReloaded).contains(tagAlreadyExists);
    }
}