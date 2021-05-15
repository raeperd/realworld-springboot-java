package io.github.raeperd.realworld.domain.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface UserRepository extends Repository<User, Long> {

    User save(User user);

    Optional<User> findFirstByEmail(Email email);

    Optional<User> findById(long id);

}
