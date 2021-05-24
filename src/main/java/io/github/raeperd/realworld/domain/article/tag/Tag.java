package io.github.raeperd.realworld.domain.article.tag;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "tags")
@Entity
public class Tag {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "value", unique = true, nullable = false)
    private String value;

    protected Tag() {
    }

    @Override
    public String toString() {
        return value;
    }
}
