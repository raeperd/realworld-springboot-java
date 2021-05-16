package io.github.raeperd.realworld.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {

    @Column(name = "image")
    private String address;

    public Image(String address) {
        this.address = address;
    }

    protected Image() {
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var image = (Image) o;
        return address.equals(image.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
