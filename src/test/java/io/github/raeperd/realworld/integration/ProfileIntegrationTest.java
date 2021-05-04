package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.raeperd.realworld.integration.IntegrationTestUtils.loginAndRememberToken;
import static io.github.raeperd.realworld.integration.IntegrationTestUtils.saveUser;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@SpringBootTest
class ProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    private final User userSaved = new User("raeperd@gmail.com", "raeperd", "password");
    private final User celebrity = new User("celeb@gmail.com", "celebrity", "psasword");

    @BeforeAll
    void initializeUserAndToken() throws Exception {
        saveUser(mockMvc, userSaved).andExpect(status().isCreated());
        saveUser(mockMvc, celebrity).andExpect(status().isCreated());
        userToken = loginAndRememberToken(mockMvc, userSaved);
    }

    @Test
    void when_get_profile_expect_return_profile() throws Exception {
        mockMvc.perform(get("/profiles/{username}", userSaved.getUsername())
                .header(AUTHORIZATION, "Token " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("profile").exists())
                .andExpect(jsonPath("profile.username", is(userSaved.getUsername())))
                .andExpect(jsonPath("profile.bio").hasJsonPath())
                .andExpect(jsonPath("profile.image").hasJsonPath())
                .andExpect(jsonPath("profile.following").isBoolean());
    }

    @Test
    void when_follow_user_expect_return_following_profile() throws Exception {
        mockMvc.perform(post("/profiles/{username}/follow", celebrity.getUsername())
                .header(AUTHORIZATION, "Token " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("profile").exists())
                .andExpect(jsonPath("profile.following", is(true)));

        mockMvc.perform(get("/profiles/{username}", celebrity.getUsername())
                .header(AUTHORIZATION, "Token " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("profile").exists())
                .andExpect(jsonPath("profile.following", is(true)));

        mockMvc.perform(delete("/profiles/{username}/follow", celebrity.getUsername())
                .header(AUTHORIZATION, "Token " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("profile").exists())
                .andExpect(jsonPath("profile.following", is(false)));
    }

}