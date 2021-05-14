package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.user.User;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.lang.String.valueOf;

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
class UserModel {

    String email;
    String username;
    String token;
    String bio;
    String image;

    // TODO: Return valid token
    static UserModel fromUser(User user) {
        return new UserModel(
                valueOf(user.getEmail()),
                "",
                valueOf(user.getName()),
                "",
                "");
    }

}
