package io.github.raeperd.realworld.application.user.profile;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.user.profile.Profile;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("profile")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
public class ProfileResponseDTO {

    private final String username;
    private final String bio;
    private final String image;
    private final boolean following;

    public static ProfileResponseDTO fromProfile(Profile profile) {
        return new ProfileResponseDTO(profile.getUsername(), profile.getBio(), profile.getImage(), profile.isFollowing());
    }

    private ProfileResponseDTO(String username, String bio, String image, boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }
}
