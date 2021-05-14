package io.github.raeperd.realworld.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class UserName {

    @Column(name = "name", nullable = false)
    private String name;

    UserName(String name) {
        this.name = name;
    }

    protected UserName() {
    }
}
