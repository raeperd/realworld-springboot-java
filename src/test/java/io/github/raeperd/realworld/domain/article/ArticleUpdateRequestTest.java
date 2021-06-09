package io.github.raeperd.realworld.domain.article;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.raeperd.realworld.domain.article.ArticleUpdateRequest.builder;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ArticleUpdateRequestTest {

    @Test
    void when_articleUpdateRequest_created_without_field_expect_get_return_empty() {
        final var requestWithoutFields = builder().build();

        assertThat(requestWithoutFields.getTitleToUpdate()).isEmpty();
        assertThat(requestWithoutFields.getDescriptionToUpdate()).isEmpty();
        assertThat(requestWithoutFields.getBodyToUpdate()).isEmpty();
    }

    @Test
    void when_articleUpdateRequest_created_with_all_fields_expect_all_fields(@Mock ArticleTitle title) {
        final var requestWithAllFields = builder()
                .titleToUpdate(title)
                .descriptionToUpdate("descriptionToUpdate")
                .bodyToUpdate("bodyToUpdate")
                .build();

        assertThat(requestWithAllFields).hasNoNullFieldsOrProperties();
        assertThat(requestWithAllFields.getTitleToUpdate()).contains(title);
        assertThat(requestWithAllFields.getDescriptionToUpdate()).contains("descriptionToUpdate");
        assertThat(requestWithAllFields.getBodyToUpdate()).contains("bodyToUpdate");
    }

}