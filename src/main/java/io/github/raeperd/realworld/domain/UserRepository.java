package io.github.raeperd.realworld.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmailAndPassword(String email, String password);
}
