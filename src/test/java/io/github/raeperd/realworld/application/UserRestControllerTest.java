package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.domain.User;
import io.github.raeperd.realworld.domain.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.raeperd.realworld.domain.User.createNewUser;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void when_post_login_expect_userService_login_called() throws Exception {
        final var loginDTO = new UserLoginRequestDTO("email", "password");
        given(userService.login(loginDTO.getEmail(), loginDTO.getPassword()))
                .willReturn(of(createNewUser("", loginDTO.getEmail(), loginDTO.getPassword())));

        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        then(userService).should(times(1))
                .login(loginDTO.getEmail(), loginDTO.getPassword());
    }

    @Test
    void when_post_login_expect_valid_response() throws Exception {
        when(userService.login(anyString(), anyString()))
                .thenReturn(of(createNewUser("username", "email", "password")));

        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserLoginRequestDTO("email", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").exists())
                .andExpect(jsonPath("$.user.bio").hasJsonPath())
                .andExpect(jsonPath("$.user.image").hasJsonPath())
                .andExpect(jsonPath("$.user.token").hasJsonPath());
    }

    @Test
    void when_post_users_expect_userService_createUser_called() throws Exception {
        final var postRequestDTO = new UserPostRequestDTO("username", "email", "password");
        given(userService.createUser(any(User.class))).willReturn(postRequestDTO.toUser());

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequestDTO)));

        then(userService).should(times(1)).createUser(any(User.class));
    }

    @Test
    void when_post_users_expect_valid_response() throws Exception {
        when(userService.createUser(any(User.class)))
                .thenReturn(createNewUser("username", "email", "password"));

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserLoginRequestDTO("email", "password"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").exists())
                .andExpect(jsonPath("$.user.bio").hasJsonPath())
                .andExpect(jsonPath("$.user.image").hasJsonPath())
                .andExpect(jsonPath("$.user.token").hasJsonPath());
    }

}