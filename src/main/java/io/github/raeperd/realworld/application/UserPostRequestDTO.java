package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.user.Email;
import io.github.raeperd.realworld.domain.user.UserName;
import io.github.raeperd.realworld.domain.user.UserSignUpRequest;
import lombok.Value;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
class UserPostRequestDTO {

    @javax.validation.constraints.Email
    String email;
    @NotBlank
    String username;
    @NotBlank
    String password;

    UserSignUpRequest toSignUpRequest() {
        return new UserSignUpRequest(
                new Email(email),
                new UserName(username),
                password);
    }

}
