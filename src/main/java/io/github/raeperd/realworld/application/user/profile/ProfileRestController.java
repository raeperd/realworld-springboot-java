package io.github.raeperd.realworld.application.user.profile;

import io.github.raeperd.realworld.domain.user.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/profiles")
@RestController
public class ProfileRestController {

    private final ProfileService profileService;

    public ProfileRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ProfileResponseDTO getProfile(@PathVariable String username) {
        return ProfileResponseDTO.fromProfile(
                profileService.viewProfileByUsername(username));
    }

    @PostMapping("/{username}/follow")
    public ProfileResponseDTO followUser(@PathVariable String username) {
        return ProfileResponseDTO.fromProfile(
                profileService.followByUsername(username));
    }
}
