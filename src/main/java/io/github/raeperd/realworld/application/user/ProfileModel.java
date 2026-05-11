package io.github.raeperd.realworld.application.user;

import io.github.raeperd.realworld.domain.user.Profile;

import static java.lang.String.valueOf;

public record ProfileModel(ProfileModelNested profile) {

    public static ProfileModel fromProfile(Profile profile) {
        return new ProfileModel(ProfileModelNested.fromProfile(profile));
    }

    public record ProfileModelNested(String username, String bio, String image, boolean following) {

        public static ProfileModelNested fromProfile(Profile profile) {
            return new ProfileModelNested(valueOf(profile.getUserName()),
                    profile.getBio(),
                    valueOf(profile.getImage()),
                    profile.isFollowing());
        }
    }
}
