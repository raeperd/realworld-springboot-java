package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserService;
import io.github.raeperd.realworld.domain.user.UserSignUpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static io.github.raeperd.realworld.domain.user.UserTestUtils.userWithEmailAndName;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @MethodSource("provideInvalidPostDTO")
    @ParameterizedTest
    void when_post_user_with_invalid_body_expect_status_badRequest(UserPostRequestDTO dto) throws Exception {
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_post_user_expect_valid_user_model() throws Exception {
        when(userService.signUp(any(UserSignUpRequest.class))).thenReturn(sampleUser());

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePostDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("user").hasJsonPath())
                .andExpect(jsonPath("user.email").isString())
                .andExpect(jsonPath("user.token").isString())
                .andExpect(jsonPath("user.username").isString())
                .andExpect(jsonPath("user.bio").isString())
                .andExpect(jsonPath("user.image").isString());
    }

    private static Stream<Arguments> provideInvalidPostDTO() {
        return Stream.of(
                Arguments.of(new UserPostRequestDTO("not-email", "username", "password")),
                Arguments.of(new UserPostRequestDTO("user@email.com", "", "password")),
                Arguments.of(new UserPostRequestDTO("user@email.com", "username", ""))
        );
    }

    private User sampleUser() {
        return userWithEmailAndName("user@email.com", "username");
    }

    private UserPostRequestDTO samplePostDTO() {
        return new UserPostRequestDTO("user@email.com", "username", "password");
    }

}