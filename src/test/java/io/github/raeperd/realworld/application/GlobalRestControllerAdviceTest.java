package io.github.raeperd.realworld.application;

import io.github.raeperd.realworld.application.user.UserRestController;
import io.github.raeperd.realworld.domain.jwt.JWTParser;
import io.github.raeperd.realworld.domain.jwt.WithMockJWT;
import io.github.raeperd.realworld.domain.user.UserContextHolder;
import io.github.raeperd.realworld.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJWT
@AutoConfigureMockMvc
@WebMvcTest(controllers = UserRestController.class)
class GlobalRestControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JWTParser jwtParser;
    @MockBean
    private UserContextHolder userContextHolder;
    @MockBean
    private UserService userService;

    @Test
    void when_internal_exception_without_message_expect_exception_name_in_body() throws Exception {
        when(userService.refreshUserAuthorization()).thenThrow(new IllegalStateException());

        mockMvc.perform(get("/user"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("errors.body[0]").value(IllegalStateException.class.getName()));
    }

    @Test
    void when_internal_exception_with_message_expect_message_in_body() throws Exception {
        final var message = "some message";
        when(userService.refreshUserAuthorization()).thenThrow(new IllegalStateException(message));

        mockMvc.perform(get("/user"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("errors.body[0]").value(message));
    }

}