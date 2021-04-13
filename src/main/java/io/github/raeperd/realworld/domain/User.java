package io.github.raeperd.realworld.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private long id;

    private String email;
    private String password;
    private String username;
    private String bio;
    private String image;

    protected User() {
    }

    public static User fromEmailAndPassword(String email, String password) {
        return new User(email, password);
    }

    private User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }
}
