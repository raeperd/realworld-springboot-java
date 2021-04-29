package io.github.raeperd.realworld.domain.user;

import java.util.Optional;

public interface UserContextHolder {

    Optional<User> getCurrentUser();

}
