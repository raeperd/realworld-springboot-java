package io.github.raeperd.realworld.domain.user;

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
        final var userToSee = userRepository.findFirstByProfileUsername(username)
                .orElseThrow(IllegalArgumentException::new);
        return userContextHolder.getCurrentUser()
                .map(currentUser -> currentUser.viewProfile(userToSee))
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Profile followByUsername(String username) {
        final var userToFollow = userRepository.findFirstByProfileUsername(username)
                .orElseThrow(IllegalArgumentException::new);
        return userContextHolder.getCurrentUser()
                .map(currentUser -> currentUser.followUser(userToFollow))
                .map(currentUser -> currentUser.viewProfile(userToFollow))
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Profile unfollowByUsername(String username) {
        final var userToUnfollow = userRepository.findFirstByProfileUsername(username)
                .orElseThrow(IllegalArgumentException::new);
        return userContextHolder.getCurrentUser()
                .map(currentUser -> currentUser.unfollowUser(userToUnfollow))
                .map(currentUser -> currentUser.viewProfile(userToUnfollow))
                .orElseThrow(IllegalStateException::new);
    }
}
