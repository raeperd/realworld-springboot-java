package io.github.raeperd.realworld.domain.user;

import javax.persistence.Embeddable;

@Embeddable
public class Email {

    private String address;

    public static Email of(String address) {
        return new Email(address);
    }

    protected Email() {
    }

    private Email(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return address;
    }

}
