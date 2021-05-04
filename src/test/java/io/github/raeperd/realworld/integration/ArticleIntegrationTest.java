package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.article.ArticlePostRequestDTO;
import io.github.raeperd.realworld.application.article.ArticlePutRequestDTO;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
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
    private static final String SAMPLE_ARTICLE_SLUG = "SAMPLE_ARTICLE_SLUG";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    private final User userSaved = new User("raeperd@gmail.com", "raeperd", "password");
    private final String sampleTag = "some-tag";

    @BeforeAll
    void initializeUser() throws Exception {
        saveUser(mockMvc, userSaved).andExpect(status().isCreated());
        userToken = loginAndRememberToken(mockMvc, userSaved);
    }

    @BeforeEach
    void createSampleArticle() throws Exception {
        postSampleArticle().andExpect(status().isOk());
    }

    private ResultActions postSampleArticle() throws Exception {
        return postArticle(new ArticlePostRequestDTO(SAMPLE_ARTICLE_SLUG, "description", "body", Set.of(sampleTag)));
    }

    private ResultActions postArticle(ArticlePostRequestDTO postRequestDTO) throws Exception {
        return mockMvc.perform(post("/articles")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken)
                .content(objectMapper.writeValueAsString(postRequestDTO)));
    }

    @AfterEach
    void cleanSampleArticle() throws Exception {
        deleteSampleArticle();
    }

    private ResultActions deleteSampleArticle() throws Exception {
        return deleteArticleBySlug(SAMPLE_ARTICLE_SLUG);
    }

    private ResultActions deleteArticleBySlug(String slug) throws Exception {
        return mockMvc.perform(delete("/articles/{slug}", slug)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken));
    }

    @MethodSource("provideArticlePostRequests")
    @ParameterizedTest
    void when_create_article_expect_return_valid_response(ArticlePostRequestDTO requestDTO) throws Exception {
        final var resultActions = postArticle(requestDTO);

        andExpectValidSingleArticleResponse(resultActions)
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

    private ResultActions andExpectValidSingleArticleResponse(ResultActions resultActions) throws Exception {
        return andExpectValidArticleResponse(resultActions, "article");
    }

    private ResultActions andExpectValidMultipleArticleResponse(ResultActions resultActions) throws Exception {
        return andExpectValidArticleResponse(resultActions, "$.articles[0]");
    }

    private ResultActions andExpectValidArticleResponse(ResultActions resultActions, String articlePath) throws Exception {
        return resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(articlePath).exists())
                .andExpect(jsonPath(articlePath + ".author").exists())
                .andExpect(jsonPath(articlePath + ".author.username").exists())
                .andExpect(jsonPath(articlePath + ".slug").isString())
                .andExpect(jsonPath(articlePath + ".title").isString())
                .andExpect(jsonPath(articlePath + ".description").isString())
                .andExpect(jsonPath(articlePath + ".body").isString())
                .andExpect(jsonPath(articlePath + ".tagList").isArray())
                .andExpect(jsonPath(articlePath + ".createdAt", matchesPattern(ISO_8601_PATTERN)))
                .andExpect(jsonPath(articlePath + ".updatedAt", matchesPattern(ISO_8601_PATTERN)))
                .andExpect(jsonPath(articlePath + ".favorited").isBoolean())
                .andExpect(jsonPath(articlePath + ".favoritesCount").isNumber());
    }

    @Test
    void when_get_article_expect_return_valid_response() throws Exception {
        final var resultActions = mockMvc.perform(get("/articles/{slug}", SAMPLE_ARTICLE_SLUG)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken));

        andExpectValidSingleArticleResponse(resultActions);
    }

    @Test
    void when_get_articles_expect_return_valid_response() throws Exception {
        final var resultActions = mockMvc.perform(get("/articles")
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken));

        andExpectValidMultipleArticleResponse(resultActions);
    }

    @Test
    void when_get_articles_without_authentication_expect_return_valid_response() throws Exception {
        final var resultActions = mockMvc.perform(get("/articles")
                .accept(APPLICATION_JSON));

        andExpectValidMultipleArticleResponse(resultActions);
    }

    @Test
    void when_put_article_expect_return_valid_response() throws Exception {
        final var articlePostRequestDTO = new ArticlePostRequestDTO("title-to-updated", "description", "body", null);
        postArticle(articlePostRequestDTO);
        final var articlePutRequestDTO = new ArticlePutRequestDTO("title-updated", null, null);

        final var resultActions = mockMvc.perform(put("/articles/{slug}", articlePostRequestDTO.getTitle())
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken)
                .content(objectMapper.writeValueAsString(articlePutRequestDTO)));

        andExpectValidSingleArticleResponse(resultActions);
        deleteArticleBySlug(articlePutRequestDTO.getTitle()).andExpect(status().isOk());
    }

    @Test
    void when_delete_article_expect_status_ok() throws Exception {
        final var articlePostRequestDTO = new ArticlePostRequestDTO("title-to-be-deleted", "description", "body", null);
        postArticle(articlePostRequestDTO);

        deleteArticleBySlug(articlePostRequestDTO.getTitle())
                .andExpect(status().isOk());
    }

    @Test
    void when_favorite_article_expect_valid_response() throws Exception {
        final var resultActions = postSampleArticleFavorite();

        andExpectValidSingleArticleResponse(resultActions)
                .andExpect(jsonPath("article.favorited", is(true)));
    }

    private ResultActions postSampleArticleFavorite() throws Exception {
        return mockMvc.perform(post("/articles/{slug}/favorite", SAMPLE_ARTICLE_SLUG)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken));
    }

    @Test
    void when_delete_favorite_article_expect_valid_response() throws Exception {
        postSampleArticleFavorite();

        final var resultActions = deleteArticleFavoriteBySlug();

        andExpectValidSingleArticleResponse(resultActions)
                .andExpect(jsonPath("article.favorited", is(false)));
    }

    private ResultActions deleteArticleFavoriteBySlug() throws Exception {
        return mockMvc.perform(delete("/articles/{slug}/favorite", SAMPLE_ARTICLE_SLUG)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + userToken));
    }

}
