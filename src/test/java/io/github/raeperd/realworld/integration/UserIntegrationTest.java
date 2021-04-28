package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.UserLoginRequestDTO;
import io.github.raeperd.realworld.application.UserPostRequestDTO;
import io.github.raeperd.realworld.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private final User savedUser = new User("raeperd@gmail.com", "raeperd", SAVED_USER_PASSWORD);

    @BeforeAll
    void initializeUser() throws Exception {
        final var userPostRequestDTO = new UserPostRequestDTO(savedUser.getUsername(), savedUser.getEmail(), SAVED_USER_PASSWORD);

        mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPostRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void when_post_user_expect_return_created_user_with_token() throws Exception {
        final var userPostRequestDTO = new UserPostRequestDTO("username", "email", "password");

        mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPostRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("user").exists())
                .andExpect(jsonPath("user.email", is(userPostRequestDTO.getEmail())))
                .andExpect(jsonPath("user.username", is(userPostRequestDTO.getUsername())))
                .andExpect(jsonPath("user.token").isString())
                .andExpect(jsonPath("user.bio").doesNotExist())
                .andExpect(jsonPath("user.image").doesNotExist())
                .andExpect(jsonPath("user.password").doesNotHaveJsonPath());
    }

    @Test
    void when_login_expect_return_user_with_token() throws Exception {
        final var userLoginRequestDTO = new UserLoginRequestDTO(savedUser.getEmail(), SAVED_USER_PASSWORD);

        login().andExpect(status().isOk())
                .andExpect(jsonPath("user").exists())
                .andExpect(jsonPath("user.email", is(userLoginRequestDTO.getEmail())))
                .andExpect(jsonPath("user.token").isString())
                .andExpect(jsonPath("user.username").hasJsonPath())
                .andExpect(jsonPath("user.bio").hasJsonPath())
                .andExpect(jsonPath("user.image").hasJsonPath());
    }

    @Test
    void when_get_user_after_login_expect_current_user_response() throws Exception {
        final var token = loginAndRememberToken();

        mockMvc.perform(get("/user")
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("user").exists());
    }

    private ResultActions login() throws Exception {
        final var userLoginRequestDTO = new UserLoginRequestDTO(savedUser.getEmail(), SAVED_USER_PASSWORD);
        return mockMvc.perform(post("/users/login")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginRequestDTO)));
    }

    private String loginAndRememberToken() throws Exception {
        final var responseBodyAsString = login()
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(responseBodyAsString)
                .get("user").get("token").textValue();
    }
}
