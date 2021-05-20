package io.github.raeperd.realworld;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class IntegrationTestUtils {

    static final String EMAIL = "user@email.com";
    static final String PASSWORD = "password";
    static final String USERNAME = "username";

    static final String CELEB_EMAIL = "celeb_" + EMAIL;
    static final String CELEB_USERNAME = "celeb_" + USERNAME;

    private IntegrationTestUtils() {
    }

    public static ResultMatcher validUserModel() {
        return matchAll(jsonPath("user").hasJsonPath(),
                jsonPath("user.email").isString(),
                jsonPath("user.token").isString(),
                jsonPath("user.username").isString(),
                jsonPath("user.bio").isString(),
                jsonPath("user.image").isString());
    }

    public static ResultMatcher validProfileModel() {
        return matchAll(
                jsonPath("profile").hasJsonPath(),
                jsonPath("profile.username").isString(),
                jsonPath("profile.bio").hasJsonPath(),
                jsonPath("profile.image").hasJsonPath(),
                jsonPath("profile.following").isBoolean());
    }
}
