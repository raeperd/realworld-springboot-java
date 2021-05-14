package io.github.raeperd.realworld.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserName {

    @Column(name = "name", nullable = false)
    private String name;

    public UserName(String name) {
        this.name = name;
    }

    protected UserName() {
    }

    @Override
    public String toString() {
        return name;
    }
}
