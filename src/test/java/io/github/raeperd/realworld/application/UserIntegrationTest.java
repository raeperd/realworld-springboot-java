package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.github.raeperd.realworld.application.UserRestControllerTest.validUserModel;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_post_user_expect_status_ok() throws Exception {
        postUser("userSaved@email.com", "usernameSaved", "password-saved")
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Test
    void when_get_user_expect_status_ok() throws Exception {
        final var token = postUserAndRememberToken("user@email.com", "username", "password");

        mockMvc.perform(get("/user")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Test
    void when_login_with_invalid_credential_expect_notFound_status() throws Exception {
        postUserLogin("not-saved-user@email.com", "password")
                .andExpect(status().isNotFound());
    }

    @Test
    void when_login_expect_status_ok() throws Exception {
        postUser("userSaved@email.com", "usernameSaved", "password-saved")
                .andExpect(status().isOk());

        postUserLogin("userSaved@email.com", "password-saved")
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    private String postUserAndRememberToken(String email, String username, String password) throws Exception {
        final var contentAsString = postUser(email, username, password)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(contentAsString)
                .get("user").get("token").textValue();
    }

    private ResultActions postUser(String email, String username, String password) throws Exception {
        final var dto = new UserPostRequestDTO(email, username, password);
        return mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }

    private ResultActions postUserLogin(String email, String password) throws Exception {
        final var dto = new UserLoginRequestDTO(email, password);
        return mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }

}
