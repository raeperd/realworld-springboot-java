package io.github.raeperd.realworld.application.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.user.AuthorizedUser;
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

    public static UserResponseDTO fromAuthorizedUser(AuthorizedUser user) {
        return new UserResponseDTO(user);
    }

    private UserResponseDTO(AuthorizedUser user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.token = user.getToken();
    }
}
