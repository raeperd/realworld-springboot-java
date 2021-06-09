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
    public Profile viewProfile(long viewerId, UserName usernameToView) {
        final var viewer = userFindService.findById(viewerId).orElseThrow(NoSuchElementException::new);
        return userFindService.findByUsername(usernameToView)
                .map(viewer::viewProfile)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Profile viewProfile(UserName userName) {
        return userFindService.findByUsername(userName)
                .map(User::getProfile)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Profile followAndViewProfile(long followerId, UserName followeeUserName) {
        final var followee = userFindService.findByUsername(followeeUserName).orElseThrow(NoSuchElementException::new);
        return userFindService.findById(followerId)
                .map(follower -> follower.followUser(followee))
                .map(follower -> follower.viewProfile(followee))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Profile unfollowAndViewProfile(long followerId, UserName followeeUserName) {
        final var followee = userFindService.findByUsername(followeeUserName).orElseThrow(NoSuchElementException::new);
        return userFindService.findById(followerId)
                .map(follower -> follower.unfollowUser(followee))
                .map(follower -> follower.viewProfile(followee))
                .orElseThrow(NoSuchElementException::new);
    }
}
