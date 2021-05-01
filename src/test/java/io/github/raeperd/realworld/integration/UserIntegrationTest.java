package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.user.UserUpdateRequestDTO;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.github.raeperd.realworld.integration.IntegrationTestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@SpringBootTest
class UserIntegrationTest {

    private static final String SAVED_USER_PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    private final User userSaved = new User("raeperd@gmail.com", "raeperd", SAVED_USER_PASSWORD);

    @BeforeAll
    void initializeUserAndToken() throws Exception {
        saveUser(mockMvc, userSaved).andExpect(status().isCreated());
        userToken = loginAndRememberToken(mockMvc, userSaved);
    }

    @Test
    void when_post_user_expect_return_created_user_with_token() throws Exception {
        saveUser(mockMvc, userSaved)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("user").exists())
                .andExpect(jsonPath("user.email", is(userSaved.getEmail())))
                .andExpect(jsonPath("user.username", is(userSaved.getUsername())))
                .andExpect(jsonPath("user.token").isString())
                .andExpect(jsonPath("user.bio").doesNotExist())
                .andExpect(jsonPath("user.image").doesNotExist())
                .andExpect(jsonPath("user.password").doesNotHaveJsonPath());
    }

    @Test
    void when_login_expect_return_user_with_token() throws Exception {
        login(mockMvc, userSaved)
                .andExpect(status().isOk())
                .andExpect(jsonPath("user").exists())
                .andExpect(jsonPath("user.email", is(userSaved.getEmail())))
                .andExpect(jsonPath("user.token").isString())
                .andExpect(jsonPath("user.username", is(userSaved.getUsername())))
                .andExpect(jsonPath("user.bio").hasJsonPath())
                .andExpect(jsonPath("user.image").hasJsonPath());
    }

    @Test
    void when_get_user_after_login_expect_current_user_response() throws Exception {
        mockMvc.perform(get("/user")
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("user").exists());
    }

    @Test
    void when_put_user_after_login_expect_return_user_response() throws Exception {
        final var originalEmail = userSaved.getEmail();
        final var emailToUpdate = "updatedUser@email.com";

        updateUserEmail(emailToUpdate)
                .andExpect(jsonPath("user").exists())
                .andExpect(jsonPath("user.email", is(emailToUpdate)));

        updateUserEmail(originalEmail);
    }

    private ResultActions updateUserEmail(String emailToUpdate) throws Exception {
        final var userUpdateDTO = new UserUpdateRequestDTO(emailToUpdate, null, null, null, null);
        return mockMvc.perform(put("/user")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken)
                .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk());
    }


}
