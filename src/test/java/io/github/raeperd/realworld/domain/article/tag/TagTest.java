package io.github.raeperd.realworld.domain.article.tag;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @Test
    void tag_has_no_args_protected_constructor() {
        class ChildTag extends Tag {
            ChildTag() {
                super();
            }
        }
        assertThat(new ChildTag()).isInstanceOf(Tag.class);
    }

    @Test
    void when_tag_toString_expect_return_value() {
        final var tag = new Tag();
        ReflectionTestUtils.setField(tag, "value", "some-tag");

        assertThat(tag).hasToString("some-tag");
    }

    @Test
    void when_tag_has_different_value_expect_not_equal_and_hashCode() {
        final var tag = new Tag("some-vale");
        final var tagWithDifferentValue = new Tag("some-different-value");

        assertThat(tag)
                .isNotEqualTo(tagWithDifferentValue)
                .extracting(Tag::hashCode).isNotEqualTo(tagWithDifferentValue.hashCode());
    }

    @Test
    void when_tag_has_same_value_expect_equal() {
        final var tag = new Tag("some-value");
        final var tagWithSameValue = new Tag("some-value");

        assertThat(tag)
                .isEqualTo(tagWithSameValue)
                .hasSameHashCodeAs(tagWithSameValue);
    }
}