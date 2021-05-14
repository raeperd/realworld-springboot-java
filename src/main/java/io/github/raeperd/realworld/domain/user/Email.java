package io.github.raeperd.realworld.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class Email {

    @Column(name = "email", nullable = false)
    private String address;

    Email(String address) {
        this.address = address;
    }

    protected Email() {
    }
}
