package io.github.raeperd.realworld.domain.article;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @Test
    void expect_tag_has_protected_no_args_constructor() {
        class ChildTag extends Tag {
            public ChildTag() {
                super();
            }
        }
        final var childTag = new ChildTag();

        assertThat(childTag).isInstanceOf(Tag.class);
    }

}