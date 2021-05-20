package io.github.raeperd.realworld.application.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.IntegrationTestUtils;
import io.github.raeperd.realworld.application.security.WithMockJWTUser;
import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import io.github.raeperd.realworld.domain.jwt.JWTSerializer;
import io.github.raeperd.realworld.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static io.github.raeperd.realworld.domain.user.UserTestUtils.userWithEmailAndName;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private JWTSerializer jwtSerializer;
    @MockBean
    private JWTDeserializer jwtDeserializer;

    @BeforeEach
    void mockJwtSerializer() {
        when(jwtSerializer.jwtFromUser(any())).thenReturn("MOCKED_TOKEN");
    }

    @MethodSource("provideInvalidPostDTO")
    @ParameterizedTest
    void when_post_user_with_invalid_body_expect_status_badRequest(UserPostRequestDTO dto) throws Exception {
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_post_user_expect_valid_userModel() throws Exception {
        when(userService.signUp(any(UserSignUpRequest.class))).thenReturn(sampleUser());

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePostDTO())))
                .andExpect(status().isOk())
                .andExpect(IntegrationTestUtils.validUserModel());
    }

    @Test
    void when_login_user_expect_valid_userModel() throws Exception {
        when(userService.login(new Email("user@email.com"), "password")).thenReturn(of(sampleUser()));

        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleLoginDTO())))
                .andExpect(status().isOk())
                .andExpect(IntegrationTestUtils.validUserModel());
    }

    @WithMockJWTUser
    @Test
    void when_get_user_expect_valid_userModel() throws Exception {
        when(userService.findById(anyLong())).thenReturn(of(sampleUser()));

        mockMvc.perform(get("/user")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(IntegrationTestUtils.validUserModel());
    }

    @WithMockJWTUser
    @Test
    void when_put_user_expect_status_ok() throws Exception {
        when(userService.updateUser(anyLong(), any(UserUpdateRequest.class))).thenReturn(sampleUser());

        mockMvc.perform(put("/user")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePutDTO())))
                .andExpect(status().isOk())
                .andExpect(IntegrationTestUtils.validUserModel());
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

    private UserLoginRequestDTO sampleLoginDTO() {
        return new UserLoginRequestDTO("user@email.com", "password");
    }

    private UserPostRequestDTO samplePostDTO() {
        return new UserPostRequestDTO("user@email.com", "username", "password");
    }

    private UserPutRequestDTO samplePutDTO() {
        return new UserPutRequestDTO("new-user@email.com", null, null, null, null);
    }

}