package io.github.raeperd.realworld;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class IntegrationTestUtils {

    private static final Pattern ISO_8601_PATTERN = compile("^\\d{4,}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d.\\d+(?:[+-][0-2]\\d:[0-5]\\d|Z)$");

    static final String EMAIL = "user@email.com";
    static final String PASSWORD = "password";
    static final String USERNAME = "username";

    static final String CELEB_EMAIL = "celeb_" + EMAIL;
    static final String CELEB_USERNAME = "celeb_" + USERNAME;

    private IntegrationTestUtils() {
    }

    public static ResultMatcher validUserModel() {
        return matchAll(
                jsonPath("user").isMap(),
                jsonPath("user.email").isString(),
                jsonPath("user.token").isString(),
                jsonPath("user.username").isString(),
                jsonPath("user.bio").isString(),
                jsonPath("user.image").isString());
    }

    public static ResultMatcher validProfileModel() {
        return validProfileModelInPath("profile");
    }

    private static ResultMatcher validProfileModelInPath(String path) {
        return matchAll(
                jsonPath(path).isMap(),
                jsonPath(path + ".username").isString(),
                jsonPath(path + ".bio").hasJsonPath(),
                jsonPath(path + ".image").hasJsonPath(),
                jsonPath(path + ".following").isBoolean());
    }

    static ResultMatcher validSingleArticleModel() {
        return matchAll(
                jsonPath("article").isMap(),
                validArticleModelInPath("article"));
    }

    static ResultMatcher validMultipleArticleModel() {
        return matchAll(
                jsonPath("articles").isArray(),
                jsonPath("articlesCount").isNumber(),
                validArticleModelInPath("articles[0]")
        );
    }

    private static ResultMatcher validArticleModelInPath(String path) {
        return matchAll(
                jsonPath(path + ".slug").isString(),
                jsonPath(path + ".title").isString(),
                jsonPath(path + ".description").isString(),
                jsonPath(path + ".body").isString(),
                jsonPath(path + ".tagList").isNotEmpty(),
                jsonPath(path + ".createdAt", matchesPattern(ISO_8601_PATTERN)),
                jsonPath(path + ".updatedAt", matchesPattern(ISO_8601_PATTERN)),
                jsonPath(path + ".favorited").isBoolean(),
                jsonPath(path + ".favoritesCount").isNumber(),
                validProfileModelInPath(path + ".author"));
    }
}
