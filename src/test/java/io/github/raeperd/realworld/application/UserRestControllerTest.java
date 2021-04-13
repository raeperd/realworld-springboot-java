package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raeperd.realworld.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.raeperd.realworld.domain.User.fromEmailAndPassword;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.anyString;
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
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_post_login_expect_userRepository_findFirstByEmailAndPassword_called() throws Exception {
        final var loginDTO = new UserLoginRequestDTO("email", "password");
        when(userRepository.findFirstByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()))
                .thenReturn(of(fromEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword())));

        mockMvc.perform(post("/users/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        then(userRepository).should(times(1))
                .findFirstByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
    }

    @Test
    void when_post_login_expect_valid_response_body() throws Exception {
        when(userRepository.findFirstByEmailAndPassword(anyString(), anyString()))
                .thenReturn(of(fromEmailAndPassword("email", "password")));

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

}