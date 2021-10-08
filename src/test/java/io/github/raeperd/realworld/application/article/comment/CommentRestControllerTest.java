package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.application.security.WithMockJWTUser;
import io.github.raeperd.realworld.domain.article.comment.CommentService;
import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.emptySet;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {

    @MockBean
    private CommentService commentService;
    @MockBean
    private JWTDeserializer jwtDeserializer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void when_get_comments_without_auth_expect_called_commentService() throws Exception {
        final var ARTICLE_SLUG = "article-slug";
        when(commentService.getComments(ARTICLE_SLUG)).thenReturn(emptySet());

        mockMvc.perform(get("/articles/" + ARTICLE_SLUG + "/comments"))
                .andExpect(status().isOk());

        then(commentService).should(times(1)).getComments(ARTICLE_SLUG);
    }

    @WithMockJWTUser
    @Test
    void when_get_comments_with_auth_expect_called_commentService() throws Exception {
        final var ARTICLE_SLUG = "article-slug";
        when(commentService.getComments(anyLong(), eq(ARTICLE_SLUG))).thenReturn(emptySet());

        mockMvc.perform(get("/articles/" + ARTICLE_SLUG + "/comments"))
                .andExpect(status().isOk());

        then(commentService).should(times(1)).getComments(anyLong(), eq(ARTICLE_SLUG));
    }
}