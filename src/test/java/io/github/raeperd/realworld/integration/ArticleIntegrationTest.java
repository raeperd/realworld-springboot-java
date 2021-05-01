package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.article.ArticlePostRequestDTO;
import io.github.raeperd.realworld.application.user.UserLoginRequestDTO;
import io.github.raeperd.realworld.application.user.UserPostRequestDTO;
import io.github.raeperd.realworld.domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@SpringBootTest
class ArticleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final User savedUser = new User("raeperd@gmail.com", "raeperd", "password");

    @BeforeAll
    void initializeUser() throws Exception {
        final var userPostRequestDTO = new UserPostRequestDTO(savedUser.getUsername(), savedUser.getEmail(), "password");

        mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPostRequestDTO)))
                .andExpect(status().isCreated());
    }

    @MethodSource("provideArticlePostRequests")
    @ParameterizedTest
    void when_create_article_expect_return_valid_response(ArticlePostRequestDTO requestDTO) throws Exception {
        final var token = loginAndRememberToken();

        mockMvc.perform(post("/articles")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + token)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("article").exists())
                .andExpect(jsonPath("article.title", is("title")))
                .andExpect(jsonPath("article.description", is("description")))
                .andExpect(jsonPath("article.body", is("body")))
                .andExpect(jsonPath("article.tagList").isArray())
                .andExpect(jsonPath("article.slug").hasJsonPath())
                .andExpect(jsonPath("article.favorited").isBoolean())
                .andExpect(jsonPath("article.favoritesCount").isNumber());
    }

    private String loginAndRememberToken() throws Exception {
        final var responseBodyAsString = login()
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(responseBodyAsString)
                .get("user").get("token").textValue();
    }

    private ResultActions login() throws Exception {
        final var userLoginRequestDTO = new UserLoginRequestDTO(savedUser.getEmail(), "password");
        return mockMvc.perform(post("/users/login")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginRequestDTO)));
    }

    private Stream<Arguments> provideArticlePostRequests() {
        return Stream.of(
                Arguments.of(new ArticlePostRequestDTO("title", "description", "body", null)),
                Arguments.of(new ArticlePostRequestDTO("title", "description", "body", emptySet())));
    }

}
