package io.github.raeperd.realworld.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    private ProfileService profileService;

    @Mock
    private UserFindService userFindService;

    @BeforeEach
    private void initializeService() {
        this.profileService = new ProfileService(userFindService);
    }

    @Test
    void when_viewProfileFromUser_with_viewer_not_exists_expect_NoSuchElementException(@Mock UserName userName) {
        when(userFindService.findById(1L)).thenReturn(empty());

        assertThatThrownBy(() ->
                profileService.viewProfileFromUser(1L, userName)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void when_viewProfileFromUser_with_not_exists_username_expect_NoSuchElementException(@Mock User user, @Mock UserName userName) {
        when(userFindService.findById(1L)).thenReturn(of(user));
        when(userFindService.findByUsername(userName)).thenReturn(empty());

        assertThatThrownBy(() ->
                profileService.viewProfileFromUser(1L, userName)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void when_viewProfileFromUser_expect_viewer_view_found_user(@Mock UserName userName,
                                                                @Mock User viewer, @Mock User userToView,
                                                                @Mock Profile profile) {
        given(userFindService.findById(1L)).willReturn(of(viewer));
        given(userFindService.findByUsername(userName)).willReturn(of(userToView));
        given(viewer.viewProfile(userToView)).willReturn(profile);

        profileService.viewProfileFromUser(1L, userName);

        then(viewer).should(times(1)).viewProfile(userToView);
    }
}