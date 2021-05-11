package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.user.UserLoginRequestDTO;
import io.github.raeperd.realworld.application.user.UserPostRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    private final UserPostRequestDTO userSaved = new UserPostRequestDTO("raeperd", "raeperd@gmail.com", "password");
    private final UserPostRequestDTO celebrity = new UserPostRequestDTO("celebrity", "celeb@gmail.com", "password");

    @BeforeAll
    void initializeUserAndToken() throws Exception {
        saveUser(mockMvc, userSaved).andExpect(status().isCreated());
        saveUser(mockMvc, celebrity).andExpect(status().isCreated());
        userToken = loginAndRememberToken(mockMvc, new UserLoginRequestDTO(userSaved.getEmail(), userSaved.getPassword()));
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
        queryFollowUser(mockMvc, celebrity.getUsername(), userToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("profile").exists())
                .andExpect(jsonPath("profile.following", is(true)));

        mockMvc.perform(get("/profiles/{username}", celebrity.getUsername())
                .header(AUTHORIZATION, "Token " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("profile").exists())
                .andExpect(jsonPath("profile.following", is(true)));

        queryUnfollowUser(mockMvc, celebrity.getUsername(), userToken)
                .andExpect(status().isOk())
                .andExpect(jsonPath("profile").exists())
                .andExpect(jsonPath("profile.following", is(false)));
    }

    static ResultActions queryFollowUser(MockMvc mockMvc, String username, String userToken) throws Exception {
        return mockMvc.perform(post("/profiles/{username}/follow", username)
                .header(AUTHORIZATION, "Token " + userToken));
    }

    static ResultActions queryUnfollowUser(MockMvc mockMvc, String usernameToUnfollow, String userToken) throws Exception {
        return mockMvc.perform(delete("/profiles/{username}/follow", usernameToUnfollow)
                .header(AUTHORIZATION, "Token " + userToken));
    }

}