package io.github.raeperd.realworld.domain.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ProfileService {

    private final UserFindService userFindService;

    ProfileService(UserFindService userFindService) {
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public Profile viewProfileFromUser(long viewerId, UserName usernameToView) {
        final var viewer = userFindService.findById(viewerId).orElseThrow(NoSuchElementException::new);
        return userFindService.findByUsername(usernameToView)
                .map(viewer::viewProfile)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Profile viewProfileWithUserName(UserName userName) {
        return userFindService.findByUsername(userName)
                .map(User::getProfile)
                .orElseThrow(NoSuchElementException::new);
    }
}
