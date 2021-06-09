package io.github.raeperd.realworld.domain.article.tag;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "tags")
@Entity
public class Tag {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "value", unique = true, nullable = false)
    private String value;

    public Tag(String value) {
        this.value = value;
    }

    protected Tag() {
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var tag = (Tag) o;
        return value.equals(tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
