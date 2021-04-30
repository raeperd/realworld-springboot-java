package io.github.raeperd.realworld.domain.user.profile;

import io.github.raeperd.realworld.domain.user.UserContextHolder;
import io.github.raeperd.realworld.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final UserContextHolder userContextHolder;

    public ProfileService(UserRepository userRepository, UserContextHolder userContextHolder) {
        this.userRepository = userRepository;
        this.userContextHolder = userContextHolder;
    }

    @Transactional
    public Profile viewProfileByUsername(String username) {
        final var userToSee = userRepository.findFirstByUsername(username)
                .orElseThrow(IllegalArgumentException::new);
        return userContextHolder.getCurrentUser()
                .map(currentUser -> currentUser.viewProfile(userToSee))
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Profile followByUsername(String username) {
        final var userToFollow = userRepository.findFirstByUsername(username)
                .orElseThrow(IllegalArgumentException::new);
        return userContextHolder.getCurrentUser()
                .map(currentUser -> currentUser.followUser(userToFollow))
                .map(currentUser -> currentUser.viewProfile(userToFollow))
                .orElseThrow(IllegalStateException::new);
    }
}
