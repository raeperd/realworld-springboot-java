package io.github.raeperd.realworld.application.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.user.Email;
import io.github.raeperd.realworld.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
@AllArgsConstructor
public class UserPostRequestDTO {

    private final String username;
    private final String email;
    private final String password;

    public User toUser() {
        return new User(Email.of(email), username, password);
    }

}
