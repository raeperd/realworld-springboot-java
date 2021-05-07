package io.github.raeperd.realworld.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.article.ArticlePostRequestDTO;
import io.github.raeperd.realworld.domain.user.Email;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static io.github.raeperd.realworld.integration.IntegrationTestUtils.saveUserAndRememberToken;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@AutoConfigureMockMvc
@SpringBootTest
class TagIntegrationTest {

    private static final String SAMPLE_TAG_NAME = "sample-tag-with-article";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    @BeforeEach
    void initializeToken() throws Exception {
        userToken = saveUserAndRememberToken(mockMvc, new User(Email.of("some-user@email.com"), "user", "password"));
    }

    @Test
    void when_get_tags_expect_return_valid() throws Exception {
        postSampleArticleWithTag().andExpect(status().isOk());

        mockMvc.perform(get("/tags")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags", hasItem(SAMPLE_TAG_NAME)));
    }

    private ResultActions postSampleArticleWithTag() throws Exception {
        final var requestDTO = new ArticlePostRequestDTO("some-title", "description", "body", Set.of(SAMPLE_TAG_NAME));
        return mockMvc.perform(post("/articles")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken)
                .content(objectMapper.writeValueAsString(requestDTO)));
    }
}
