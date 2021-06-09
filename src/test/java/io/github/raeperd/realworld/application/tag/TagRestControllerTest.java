package io.github.raeperd.realworld.application.tag;

import io.github.raeperd.realworld.application.security.WithMockJWTUser;
import io.github.raeperd.realworld.domain.article.tag.Tag;
import io.github.raeperd.realworld.domain.article.tag.TagService;
import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import io.github.raeperd.realworld.domain.jwt.JWTSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TagRestController.class)
class TagRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TagService tagService;
    @MockBean
    private JWTSerializer jwtSerializer;
    @MockBean
    private JWTDeserializer jwtDeserializer;

    @WithMockJWTUser
    @Test
    void when_get_tags_expect_valid_tagsModel(@Mock Tag tag) throws Exception {
        when(tag.toString()).thenReturn("some-tag");
        when(tagService.findAll()).thenReturn(Set.of(tag));

        mockMvc.perform(get("/tags"))
                .andExpect(jsonPath("tags").isArray())
                .andExpect(jsonPath("tags", contains("some-tag")));
    }
}