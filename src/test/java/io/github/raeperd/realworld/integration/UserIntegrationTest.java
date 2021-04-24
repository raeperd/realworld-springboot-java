package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.UserPostRequestDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@AutoConfigureMockMvc
@SpringBootTest
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
}
