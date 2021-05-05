package io.github.raeperd.realworld.application.user.profile;

import io.github.raeperd.realworld.domain.user.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import static io.github.raeperd.realworld.application.user.profile.ProfileResponseDTO.fromProfile;

@RequestMapping("/profiles")
@RestController
public class ProfileRestController {

    private final ProfileService profileService;

    public ProfileRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ProfileResponseDTO getProfile(@PathVariable String username) {
        return fromProfile(
                profileService.viewProfileByUsername(username));
    }

    @PostMapping("/{username}/follow")
    public ProfileResponseDTO followUser(@PathVariable String username) {
        return fromProfile(
                profileService.followByUsername(username));
    }

    @DeleteMapping("/{username}/follow")
    public ProfileResponseDTO unfollowUser(@PathVariable String username) {
        return fromProfile(
                profileService.unfollowByUsername(username));
    }
}
