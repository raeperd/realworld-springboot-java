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

    @Order(1)
    @Test
    void auth_register() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(format("{\"user\":{\"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\"}}", EMAIL, PASSWORD, USERNAME)))
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

}
