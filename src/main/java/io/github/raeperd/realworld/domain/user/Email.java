package io.github.raeperd.realworld.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return address.equals(email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
