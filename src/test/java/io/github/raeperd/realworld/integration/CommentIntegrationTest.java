package io.github.raeperd.realworld.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.article.ArticlePostRequestDTO;
import io.github.raeperd.realworld.application.article.comment.CommentPostRequestDTO;
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

import java.util.Set;

import static io.github.raeperd.realworld.integration.IntegrationTestUtils.saveUserAndRememberToken;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@SpringBootTest
class CommentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String SAMPLE_ARTICLE_SLUG = "sample-comment-article-title";

    private String userToken;

    @BeforeAll
    void initializeArticle() throws Exception {
        final var authorToken = saveUserAndRememberToken(mockMvc, new User("author@email.com", "author", "password"));
        postSampleArticle(authorToken).andExpect(status().isOk());

        userToken = saveUserAndRememberToken(mockMvc, new User("user@email.com", "user", "password"));
    }

    private ResultActions postSampleArticle(String authorToken) throws Exception {
        final var requestDTO = new ArticlePostRequestDTO(SAMPLE_ARTICLE_SLUG, "description", "body", Set.of("sample-tag"));
        return mockMvc.perform(post("/articles")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + authorToken)
                .content(objectMapper.writeValueAsString(requestDTO)));
    }

    @Test
    void when_create_comment_expect_return_valid_comment() throws Exception {
        final var requestDTO = new CommentPostRequestDTO("some comments");
        mockMvc.perform(post("/articles/{slug}/comments", SAMPLE_ARTICLE_SLUG)
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

}
