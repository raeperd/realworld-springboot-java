package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.application.user.UserLoginRequestDTO;
import io.github.raeperd.realworld.application.user.UserPostRequestDTO;
import io.github.raeperd.realworld.application.user.UserRestController;
import io.github.raeperd.realworld.domain.jwt.JWTParser;
import io.github.raeperd.realworld.domain.jwt.WithMockJWT;
import io.github.raeperd.realworld.domain.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private JWTParser jwtParser;
    @MockBean
    private UserContextHolder userContextHolder;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_post_users_expect_userService_signIn_called(@Mock AuthorizedUser authorizedUser) throws Exception {
        given(userService.signIn(any(UserSignInRequest.class))).willReturn(authorizedUser);

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserPostRequestDTO("username", "email", "password"))));

        then(userService).should(times(1)).signIn(any(UserSignInRequest.class));
    }

    @Test
    void when_post_login_expect_userService_login_called(@Mock AuthorizedUser user) throws Exception {
        final var dto = new UserLoginRequestDTO("email", "password");
        given(userService.login(Email.of(dto.getEmail()), dto.getPassword())).willReturn(of(user));

        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        then(userService).should(times(1))
                .login(any(Email.class), anyString());
    }

    @Test
    void when_post_login_expect_valid_response() throws Exception {
        final var authorizedUser = mockAuthorizedUser();
        when(userService.login(any(Email.class), anyString())).thenReturn(of(authorizedUser));

        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserLoginRequestDTO("email", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("user").exists())
                .andExpect(jsonPath("user.email").exists())
                .andExpect(jsonPath("user.bio").hasJsonPath())
                .andExpect(jsonPath("user.image").hasJsonPath())
                .andExpect(jsonPath("user.token").hasJsonPath());
    }

    @Test
    void when_get_user_without_authentication_token_expect_unAuthorized_status() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockJWT
    @Test
    void when_get_user_expect_refreshUserAuthorization_called() throws Exception {
        final var authorizedUser = mockAuthorizedUser();
        given(userService.refreshUserAuthorization()).willReturn(authorizedUser);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());

        then(userService).should(times(1)).refreshUserAuthorization();
    }

    private AuthorizedUser mockAuthorizedUser() {
        final var mockedUser = mock(AuthorizedUser.class);
        when(mockedUser.getUsername()).thenReturn("");
        when(mockedUser.getEmail()).thenReturn(Email.of(""));
        when(mockedUser.getBio()).thenReturn("");
        when(mockedUser.getImage()).thenReturn("");
        when(mockedUser.getToken()).thenReturn("");
        return mockedUser;
    }

}