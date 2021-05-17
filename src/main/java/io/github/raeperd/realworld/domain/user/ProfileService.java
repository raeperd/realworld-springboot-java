package io.github.raeperd.realworld.domain.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

public class ProfileService {

    private final UserFindService userFindService;

    ProfileService(UserFindService userFindService) {
        this.userFindService = userFindService;
    }

    @Transactional
    public Profile viewProfileFromUser(long viewerId, UserName usernameToView) {
        final var viewer = userFindService.findById(viewerId).orElseThrow(NoSuchElementException::new);
        return userFindService.findByUsername(usernameToView)
                .map(viewer::viewProfile)
                .orElseThrow(NoSuchElementException::new);
    }
}
