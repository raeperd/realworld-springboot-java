package io.github.raeperd.realworld.domain;

import java.util.Optional;

public interface UserContextHolder {

    Optional<User> getCurrentUser();

}
