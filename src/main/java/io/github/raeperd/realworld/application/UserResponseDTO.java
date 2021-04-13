package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.User;
import lombok.Getter;

@JsonTypeName("user")
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@Getter
public class UserResponseDTO {

    private final String email;
    private final String token;
    private final String username;
    private final String bio;
    private final String image;

    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(user);
    }

    private UserResponseDTO(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.image = user.getImage();
        // TODO: Generate token from user somewhere else
        this.token = "";
    }
}
