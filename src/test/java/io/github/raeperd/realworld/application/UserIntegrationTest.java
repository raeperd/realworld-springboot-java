package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final UserPostRequestDTO userSaved = new UserPostRequestDTO("user@email.com",
            "user", "password");

    @Test
    void when_post_user_expect_status_ok() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSaved)))
                .andExpect(status().isOk());
    }

}
