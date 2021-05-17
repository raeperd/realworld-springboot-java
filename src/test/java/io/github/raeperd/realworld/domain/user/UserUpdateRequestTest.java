package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.raeperd.realworld.domain.user.UserUpdateRequest.builder;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserUpdateRequestTest {

    @Test
    void when_userUpdateRequest_created_without_field_expect_all_null() {
        final var requestWithoutFields = builder().build();

        assertThat(requestWithoutFields).hasAllNullFieldsOrProperties();
    }

    @Test
    void when_userUpdateRequest_created_without_field_expect_get_return_empty() {
        final var requestWithoutFields = builder().build();

        assertThat(requestWithoutFields.getEmailToUpdate()).isEmpty();
        assertThat(requestWithoutFields.getUserNameToUpdate()).isEmpty();
        assertThat(requestWithoutFields.getPasswordToUpdate()).isEmpty();
        assertThat(requestWithoutFields.getImageToUpdate()).isEmpty();
        assertThat(requestWithoutFields.getBioToUpdate()).isEmpty();
    }

    @Test
    void when_userUpdateRequest_created_with_all_field_expect_all_fields(@Mock Email emailToUpdate, @Mock UserName userNameToUpdate, @Mock Image imageToUpdate) {
        final var requestWithAllField = builder().emailToUpdate(emailToUpdate)
                .userNameToUpdate(userNameToUpdate)
                .passwordToUpdate("passwordToUpdate")
                .imageToUpdate(imageToUpdate)
                .bioToUpdate("bioToUpdate")
                .build();

        assertThat(requestWithAllField)
                .hasFieldOrPropertyWithValue("emailToUpdate", emailToUpdate)
                .hasFieldOrPropertyWithValue("userNameToUpdate", userNameToUpdate)
                .hasFieldOrPropertyWithValue("passwordToUpdate", "passwordToUpdate")
                .hasFieldOrPropertyWithValue("imageToUpdate", imageToUpdate)
                .hasFieldOrPropertyWithValue("bioToUpdate", "bioToUpdate");
    }

    @Test
    void when_userUpdateRequest_created_with_all_field_expect_get_return_field(@Mock Email emailToUpdate, @Mock UserName userNameToUpdate, @Mock Image imageToUpdate) {
        final var requestWithAllField = builder().emailToUpdate(emailToUpdate)
                .userNameToUpdate(userNameToUpdate)
                .passwordToUpdate("passwordToUpdate")
                .imageToUpdate(imageToUpdate)
                .bioToUpdate("bioToUpdate")
                .build();

        assertThat(requestWithAllField.getEmailToUpdate()).contains(emailToUpdate);
        assertThat(requestWithAllField.getUserNameToUpdate()).contains(userNameToUpdate);
        assertThat(requestWithAllField.getPasswordToUpdate()).contains("passwordToUpdate");
        assertThat(requestWithAllField.getImageToUpdate()).contains(imageToUpdate);
        assertThat(requestWithAllField.getBioToUpdate()).contains("bioToUpdate");
    }
}