package io.github.raeperd.realworld.domain.user;

import org.springframework.data.repository.Repository;

interface UserRepository extends Repository<User, Long> {

    User save(User user);

}
