package io.github.raeperd.realworld.domain.user;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "users")
@Entity
public class User {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private UserName name;

    @Embedded
    private Password password;

    User(Email email, UserName name, Password password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    protected User() {
    }

    public Email getEmail() {
        return email;
    }

    public UserName getName() {
        return name;
    }
}
