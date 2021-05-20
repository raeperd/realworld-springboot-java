package io.github.raeperd.realworld.domain.user;

public class ProfileTestUtils {

    private ProfileTestUtils() {
    }

    public static Profile profileOf(UserName userName, String bio, Image image, boolean following) {
        final var profile = new Profile(userName);
        profile.changeBio(bio);
        profile.changeImage(image);
        return profile.withFollowing(following);
    }

}
