package io.github.raeperd.realworld;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.raeperd.realworld.IntegrationTestUtils.*;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*

 Method name in this class follows /doc/Conduit.postman_collection.json

 */

@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private String secondToken;
    private int commentId;
    private int reportId;

    @Order(1)
    @Test
    void auth_register() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(format("{\"user\":{\"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\"}}", EMAIL, PASSWORD, USERNAME)))
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Order(1)
    @Test
    void auth_register_second_user() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(format("{\"user\":{\"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\"}}", SECOND_EMAIL, SECOND_PASSWORD, SECOND_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Order(2)
    @Test
    void auth_login() throws Exception {
        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(format("{\"user\":{\"email\":\"%s\", \"password\":\"%s\"}}", EMAIL, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Order(3)
    @Test
    void auth_login_and_remember_token() throws Exception{
        final var contentAsString = mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(format("{\"user\":{\"email\":\"%s\", \"password\":\"%s\"}}", EMAIL, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(validUserModel())
                .andReturn().getResponse().getContentAsString();

        token = objectMapper.readTree(contentAsString).get("user").get("token").textValue();
    }

    @Order(3)
    @Test
    void auth_login_and_remember_second_token() throws Exception{
        final var contentAsString = mockMvc.perform(post("/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(format("{\"user\":{\"email\":\"%s\", \"password\":\"%s\"}}", SECOND_EMAIL, SECOND_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(validUserModel())
                .andReturn().getResponse().getContentAsString();

        secondToken = objectMapper.readTree(contentAsString).get("user").get("token").textValue();
    }

    @Order(4)
    @Test
    void auth_current_user() throws Exception {
        mockMvc.perform(get("/user")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Order(5)
    @Test
    void auth_update_user() throws Exception {
        mockMvc.perform(put("/user")
                .header(AUTHORIZATION, "Token " + token)
                .contentType(APPLICATION_JSON)
                .content(format("{\"user\":{\"email\":\"%s\"}}", EMAIL)))
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Order(6)
    @Test
    void profiles_register_celeb() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(format("{\"user\":{\"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\"}}", CELEB_EMAIL, PASSWORD, CELEB_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(validUserModel());
    }

    @Order(7)
    @Test
    void profiles_profile() throws Exception {
        mockMvc.perform(get("/profiles/{celeb_username}", CELEB_USERNAME)
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validProfileModel());

        mockMvc.perform(get("/profiles/{celeb_username}", CELEB_USERNAME))
                .andExpect(status().isOk())
                .andExpect(validProfileModel());
    }

    @Order(8)
    @Test
    void follow_profile() throws Exception {
        mockMvc.perform(post("/profiles/{celeb_username}/follow", CELEB_USERNAME)
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validProfileModel())
                .andExpect(jsonPath("profile.following", is(true)));
    }

    @Order(9)
    @Test
    void unfollow_profile() throws Exception {
        mockMvc.perform(delete("/profiles/{celeb_username}/follow", CELEB_USERNAME)
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validProfileModel())
                .andExpect(jsonPath("profile.following", is(false)));
    }

    @Order(10)
    @Test
    void create_article() throws Exception {
        mockMvc.perform(post("/articles")
                .header(AUTHORIZATION, "Token " + token)
                .contentType(APPLICATION_JSON)
                .content("{\n" +
                        "    \"article\": {\n" +
                        "        \"title\": \"How to train your dragon\",\n" +
                        "        \"description\": \"Ever wonder how?\",\n" +
                        "        \"body\": \"Very carefully.\",\n" +
                        "        \"tagList\": [\n" +
                        "            \"dragons\",\n" +
                        "            \"training\"\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(validSingleArticleModel());
    }

    @Order(11)
    @Test
    void get_all_articles() throws Exception {
        mockMvc.perform(get("/articles?limit=20&offset=0")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validMultipleArticleModel());
    }

    @Order(11)
    @Test
    void get_all_articles_without_auth() throws Exception {
        mockMvc.perform(get("/articles?limit=20&offset=0"))
                .andExpect(status().isOk())
                .andExpect(validMultipleArticleModel());
    }

    @Order(11)
    @Test
    void get_all_articles_with_author() throws Exception {
        mockMvc.perform(get("/articles")
                .queryParam("author", USERNAME)
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validMultipleArticleModel());
    }

    @Order(11)
    @Test
    void get_all_articles_with_tag() throws Exception {
        mockMvc.perform(get("/articles")
                .queryParam("tag", "dragons")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validMultipleArticleModel());
    }

    @Order(11)
    @Test
    void get_single_article_by_slug() throws Exception {
        mockMvc.perform(get("/articles/{slug}", "how-to-train-your-dragon"))
                .andExpect(status().isOk())
                .andExpect(validSingleArticleModel());
    }

    @Order(11)
    @Test
    void put_article() throws Exception {
        mockMvc.perform(put("/articles/{slug}", "how-to-train-your-dragon")
                .header(AUTHORIZATION, "Token " + token)
                .contentType(APPLICATION_JSON)
                .content("{\"article\":{\"body\":\"With two hands\"}}"))
                .andExpect(status().isOk())
                .andExpect(validSingleArticleModel())
                .andExpect(jsonPath("article.body", is("With two hands")));
    }

    @Order(11)
    @Test
    void create_comments_for_article() throws Exception {
        final var contentAsString = mockMvc.perform(post("/articles/{slug}/comments", "how-to-train-your-dragon")
                .header(AUTHORIZATION, "Token " + token)
                .contentType(APPLICATION_JSON)
                .content("{\"comment\":{\"body\":\"Thank you so much!\"}}"))
                .andExpect(status().isOk())
                .andExpect(validSingleCommentModel())
                .andExpect(jsonPath("comment.body", is("Thank you so much!")))
                .andReturn().getResponse().getContentAsString();

        commentId = objectMapper.readTree(contentAsString).get("comment").get("id").intValue();
    }

    @Order(12)
    @Test
    void create_reports_for_comment() throws Exception {
        final var contentAsString = mockMvc.perform(post("/article/{slug}/comment/{id}/denounce", "how-to-train-your-dragon", commentId)
                        .header(AUTHORIZATION, "Token " + secondToken)
                        .contentType(APPLICATION_JSON)
                        .content("{\"denounce\":{\"body\":\"Offensive\"}}"))
                .andExpect(status().isOk())
                .andExpect(validSingleReportModel())
                .andExpect(jsonPath("denounce.body", is("Offensive")))
                .andReturn().getResponse().getContentAsString();

        reportId = objectMapper.readTree(contentAsString).get("denounce").get("id").intValue();
    }

    @Order(13)
    @Test
    void all_comments_for_article() throws Exception {
        mockMvc.perform(get("/articles/{slug}/comments", "how-to-train-your-dragon")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validMultipleCommentModel());
    }

    @Order(14)
    @Test
    void delete_comment_for_article() throws Exception {
        mockMvc.perform(delete("/articles/{slug}/comments/{id}", "how-to-train-your-dragon", commentId)
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk());
    }

    @Order(13)
    @Test
    void post_favorite_article() throws Exception {
        mockMvc.perform(post("/articles/{slug}/favorite", "how-to-train-your-dragon")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validSingleArticleModel());
    }

    @Order(14)
    @Test
    void get_articles_favorited_by_username() throws Exception {
        mockMvc.perform(get("/articles?favorited={username}", USERNAME)
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validMultipleArticleModel())
                .andExpect(jsonPath("articles[0].favorited", is(true)));
    }

    @Order(14)
    @Test
    void get_articles_favorited_by_username_not_exists() throws Exception {
        mockMvc.perform(get("/articles?favorited={username}", "jane")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("articles").isEmpty())
                .andExpect(jsonPath("articlesCount", is(0)));
    }

    @Order(14)
    @Test
    void get_feed() throws Exception {
        mockMvc.perform(get("/articles/feed")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validMultipleArticleModel())
                .andExpect(jsonPath("articles[0].favorited", is(true)));
    }

    @Order(15)
    @Test
    void unfavorite_article() throws Exception {
        mockMvc.perform(delete("/articles/{slug}/favorite", "how-to-train-your-dragon")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isOk())
                .andExpect(validSingleArticleModel())
                .andExpect(jsonPath("article.favorited", is(false)));
    }

    @Order(16)
    @Test
    void delete_article() throws Exception {
        mockMvc.perform(delete("/articles/{slug}", "how-to-train-your-dragon")
                .header(AUTHORIZATION, "Token " + token))
                .andExpect(status().isNoContent());
    }

}
