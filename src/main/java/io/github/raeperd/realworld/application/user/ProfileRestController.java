package io.github.raeperd.realworld.application.user;

import io.github.raeperd.realworld.domain.jwt.JWTPayload;
import io.github.raeperd.realworld.domain.user.ProfileService;
import io.github.raeperd.realworld.domain.user.UserName;
import io.github.raeperd.realworld.infrastructure.jwt.UserJWTPayload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static io.github.raeperd.realworld.application.user.ProfileModel.fromProfile;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("/profiles")
@RestController
class ProfileRestController {

    private final ProfileService profileService;

    ProfileRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ProfileModel getProfileByUsername(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                                             @PathVariable UserName username) {
        return ofNullable(jwtPayload)
                .map(JWTPayload::getUserId)
                .map(viewerId -> profileService.viewProfile(viewerId, username))
                .map(ProfileModel::fromProfile)
                .orElseGet(() -> fromProfile(profileService.viewProfile(username)));
    }

    @PostMapping("/{username}/follow")
    public ProfileModel followUser(@AuthenticationPrincipal UserJWTPayload followerPayload,
                                   @PathVariable UserName username) {
        return fromProfile(
                profileService.followAndViewProfile(followerPayload.getUserId(), username));
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    void handleNoSuchElementException(NoSuchElementException exception) {
        // return NOT FOUND status
    }
}
