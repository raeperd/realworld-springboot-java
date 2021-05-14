package io.github.raeperd.realworld.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

    @Column(name = "email", nullable = false)
    private String address;

    public Email(String address) {
        this.address = address;
    }

    protected Email() {
    }

    @Override
    public String toString() {
        return address;
    }
}
