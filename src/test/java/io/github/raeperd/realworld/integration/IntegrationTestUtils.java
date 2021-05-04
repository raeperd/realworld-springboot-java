package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.user.UserLoginRequestDTO;
import io.github.raeperd.realworld.application.user.UserPostRequestDTO;
import io.github.raeperd.realworld.domain.user.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class IntegrationTestUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String saveUserAndRememberToken(MockMvc mockMvc, User user) throws Exception {
        final var responseBody = saveUser(mockMvc, user).andReturn().getResponse().getContentAsString();
        return OBJECT_MAPPER.readTree(responseBody).get("user").get("token").textValue();
    }

    public static ResultActions saveUser(MockMvc mockMvc, User user) throws Exception {
        return mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(new UserPostRequestDTO(user.getUsername(), user.getEmail(), "password"))));
    }

    public static String loginAndRememberToken(MockMvc mockMvc, User user) throws Exception {
        final var responseBody = login(mockMvc, user).andReturn().getResponse().getContentAsString();
        return OBJECT_MAPPER.readTree(responseBody).get("user").get("token").textValue();
    }

    public static ResultActions login(MockMvc mockMvc, User user) throws Exception {
        return mockMvc.perform(post("/users/login")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(new UserLoginRequestDTO(user.getEmail(), "password"))));
    }
}
