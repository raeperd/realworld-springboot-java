package io.github.raeperd.realworld.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> login(String email, String password) {
        return userRepository.findFirstByEmailAndPassword(email, password);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

}
