package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.article.ArticlePostRequestDTO;
import io.github.raeperd.realworld.application.article.ArticlePutRequestDTO;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.github.raeperd.realworld.integration.IntegrationTestUtils.loginAndRememberToken;
import static io.github.raeperd.realworld.integration.IntegrationTestUtils.saveUser;
import static java.util.Collections.emptySet;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@SpringBootTest
class ArticleIntegrationTest {

    private static final Pattern ISO_8601_PATTERN = compile("^\\d{4,}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d.\\d+(?:[+-][0-2]\\d:[0-5]\\d|Z)$");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    private final User userSaved = new User("raeperd@gmail.com", "raeperd", "password");

    @BeforeAll
    void initializeUser() throws Exception {
        saveUser(mockMvc, userSaved).andExpect(status().isCreated());
        userToken = loginAndRememberToken(mockMvc, userSaved);
    }

    @MethodSource("provideArticlePostRequests")
    @ParameterizedTest
    void when_create_article_expect_return_valid_response(ArticlePostRequestDTO requestDTO) throws Exception {
        final var resultActions = createArticle(requestDTO);

        andExpectValidArticleResponse(resultActions)
                .andExpect(jsonPath("article.slug", is(requestDTO.getTitle())))
                .andExpect(jsonPath("article.title", is(requestDTO.getTitle())))
                .andExpect(jsonPath("article.description", is(requestDTO.getDescription())))
                .andExpect(jsonPath("article.body", is(requestDTO.getBody())));

        deleteArticleBySlug(requestDTO.getTitle());
    }

    private Stream<Arguments> provideArticlePostRequests() {
        return Stream.of(
                Arguments.of(new ArticlePostRequestDTO("null-tag-title", "description", "body", null)),
                Arguments.of(new ArticlePostRequestDTO("empty-tag-title", "description", "body", emptySet())));
    }

    private ResultActions createArticle(ArticlePostRequestDTO postRequestDTO) throws Exception {
        return mockMvc.perform(post("/articles")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken)
                .content(objectMapper.writeValueAsString(postRequestDTO)));
    }

    private ResultActions andExpectValidArticleResponse(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("article").exists())
                .andExpect(jsonPath("article.author").exists())
                .andExpect(jsonPath("article.author.username").exists())
                .andExpect(jsonPath("article.slug").isString())
                .andExpect(jsonPath("article.title").isString())
                .andExpect(jsonPath("article.description").isString())
                .andExpect(jsonPath("article.body").isString())
                .andExpect(jsonPath("article.tagList").isArray())
                .andExpect(jsonPath("article.createdAt", matchesPattern(ISO_8601_PATTERN)))
                .andExpect(jsonPath("article.updatedAt", matchesPattern(ISO_8601_PATTERN)))
                .andExpect(jsonPath("article.favorited").isBoolean())
                .andExpect(jsonPath("article.favoritesCount").isNumber());
    }

    private ResultActions deleteArticleBySlug(String slug) throws Exception {
        return mockMvc.perform(delete("/articles/{slug}", slug)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken));
    }

    @Test
    void when_get_article_expect_return_valid_response() throws Exception {
        final var requestDTO = new ArticlePostRequestDTO("title-to-get", "description", "body", emptySet());
        createArticle(requestDTO);

        final var resultActions = mockMvc.perform(get("/articles/{slug}", requestDTO.getTitle())
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken));

        andExpectValidArticleResponse(resultActions);
        deleteArticleBySlug(requestDTO.getTitle());
    }

    @Test
    void when_put_article_expect_return_valid_response() throws Exception {
        final var articlePostRequestDTO = new ArticlePostRequestDTO("title-to-update", "description", "body", emptySet());
        createArticle(articlePostRequestDTO);
        final var articlePutRequestDTO = new ArticlePutRequestDTO("title-updated", null, null);

        final var resultActions = mockMvc.perform(put("/articles/{slug}", articlePostRequestDTO.getTitle())
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken)
                .content(objectMapper.writeValueAsString(articlePutRequestDTO)));

        andExpectValidArticleResponse(resultActions);
        deleteArticleBySlug(articlePutRequestDTO.getTitle())
                .andExpect(status().isOk());
    }

    @Test
    void when_delete_article_expect_status_ok() throws Exception {
        final var requestDTO = new ArticlePostRequestDTO("title-to-delete", "description", "body", emptySet());
        createArticle(requestDTO);

        deleteArticleBySlug(requestDTO.getTitle())
                .andExpect(status().isOk());
    }

}
