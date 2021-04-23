package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.domain.AuthorizedUser;
import io.github.raeperd.realworld.domain.User;
import io.github.raeperd.realworld.domain.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_post_users_expect_userService_createUser_called(@Mock AuthorizedUser authorizedUser) throws Exception {
        given(userService.signUp(any(User.class))).willReturn(authorizedUser);

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserPostRequestDTO("username", "email", "password"))));

        then(userService).should(times(1)).signUp(any(User.class));
    }

    @Test
    void when_post_users_expect_valid_response_body(@Mock AuthorizedUser authorizedUser) throws Exception {
        when(userService.signUp(any(User.class))).thenReturn(authorizedUser);

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserPostRequestDTO("username", "email", "password"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("user").hasJsonPath())
                .andExpect(jsonPath("user.email").hasJsonPath())
                .andExpect(jsonPath("user.username").hasJsonPath())
                .andExpect(jsonPath("user.bio").hasJsonPath())
                .andExpect(jsonPath("user.image").hasJsonPath())
                .andExpect(jsonPath("user.token").hasJsonPath());
    }

    @Test
    void when_post_login_expect_userService_login_called(@Mock AuthorizedUser user) throws Exception {
        final var loginDTO = new UserLoginRequestDTO("email", "password");
        given(userService.login(loginDTO.getEmail(), loginDTO.getPassword())).willReturn(of(user));

        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        then(userService).should(times(1))
                .login(loginDTO.getEmail(), loginDTO.getPassword());
    }

    @Test
    void when_post_login_expect_valid_response() throws Exception {
        final var authorizedUser = mockAuthorizedUser();
        when(userService.login(anyString(), anyString())).thenReturn(of(authorizedUser));

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

    private AuthorizedUser mockAuthorizedUser() {
        final var mockedUser = mock(AuthorizedUser.class);
        when(mockedUser.getUsername()).thenReturn("");
        when(mockedUser.getEmail()).thenReturn("");
        when(mockedUser.getBio()).thenReturn("");
        when(mockedUser.getImage()).thenReturn("");
        when(mockedUser.getToken()).thenReturn("");
        return mockedUser;
    }

}