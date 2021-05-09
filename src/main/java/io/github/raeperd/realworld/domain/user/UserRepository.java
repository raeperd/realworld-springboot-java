package io.github.raeperd.realworld.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmailAndPassword(Email email, String password);

    Optional<User> findFirstByProfileUsername(String username);

}
