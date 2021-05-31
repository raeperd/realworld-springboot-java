package io.github.raeperd.realworld.application.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.security.WithMockJWTUser;
import io.github.raeperd.realworld.domain.article.ArticleService;
import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJWTUser
@WebMvcTest(ArticleRestController.class)
class ArticleRestControllerTest {

    @MockBean
    private ArticleService articleService;
    @MockBean
    private JWTDeserializer jwtDeserializer;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @MethodSource("provideInvalidPostDTO")
    @ParameterizedTest
    void when_post_article_with_invalid_body_expect_status_badRequest(ArticlePostRequestDTO invalidDTO) throws Exception {
        mockMvc.perform(post("/articles")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidPostDTO() {
        return Stream.of(
                Arguments.of(new ArticlePostRequestDTO(null, "description", "body", emptySet())),
                Arguments.of(new ArticlePostRequestDTO("title", null, "body", emptySet())),
                Arguments.of(new ArticlePostRequestDTO("title", "description", null, emptySet())),
                Arguments.of(new ArticlePostRequestDTO("title", "description", "body", null)),
                Arguments.of(new ArticlePostRequestDTO(" ", "description", "body", emptySet())),
                Arguments.of(new ArticlePostRequestDTO("title", " ", "body", emptySet())),
                Arguments.of(new ArticlePostRequestDTO("title", "description", " ", emptySet())));
    }
}