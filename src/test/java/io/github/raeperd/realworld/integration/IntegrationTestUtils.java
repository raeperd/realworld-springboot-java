package io.github.raeperd.realworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.user.UserLoginRequestDTO;
import io.github.raeperd.realworld.application.user.UserPostRequestDTO;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class IntegrationTestUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String saveUserAndRememberToken(MockMvc mockMvc, UserPostRequestDTO dto) throws Exception {
        final var responseBody = saveUser(mockMvc, dto).andReturn().getResponse().getContentAsString();
        return OBJECT_MAPPER.readTree(responseBody).get("user").get("token").textValue();
    }

    public static ResultActions saveUser(MockMvc mockMvc, UserPostRequestDTO dto) throws Exception {
        return mockMvc.perform(post("/users")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(dto)));
    }

    public static String loginAndRememberToken(MockMvc mockMvc, UserLoginRequestDTO dto) throws Exception {
        final var responseBody = login(mockMvc, dto).andReturn().getResponse().getContentAsString();
        return OBJECT_MAPPER.readTree(responseBody).get("user").get("token").textValue();
    }

    public static ResultActions login(MockMvc mockMvc, UserLoginRequestDTO dto) throws Exception {
        return mockMvc.perform(post("/users/login")
                .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(dto)));
    }
}
