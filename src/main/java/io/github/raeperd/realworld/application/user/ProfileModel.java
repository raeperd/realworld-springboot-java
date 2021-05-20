package io.github.raeperd.realworld.application.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.raeperd.realworld.domain.user.Profile;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.lang.String.valueOf;

@JsonTypeName("profile")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Value
class ProfileModel {

    String username;
    String bio;
    String image;
    boolean following;

    public static ProfileModel fromProfile(Profile profile) {
        return new ProfileModel(valueOf(profile.getUserName()),
                profile.getBio(),
                valueOf(profile.getImage()),
                profile.isFollowing());
    }
}
