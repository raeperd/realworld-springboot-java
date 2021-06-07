package io.github.raeperd.realworld.domain.user;

import io.github.raeperd.realworld.domain.article.Article;
import io.github.raeperd.realworld.domain.article.ArticleContents;
import io.github.raeperd.realworld.domain.article.ArticleUpdateRequest;
import io.github.raeperd.realworld.domain.article.comment.Comment;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "users")
@Entity
public class User {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Profile profile;

    @Embedded
    private Password password;

    @JoinTable(name = "user_followings",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id", referencedColumnName = "id"))
    @OneToMany(cascade = REMOVE)
    private Set<User> followingUsers = new HashSet<>();

    @ManyToMany(mappedBy = "userFavorited")
    private Set<Article> articleFavorited = new HashSet<>();

    static User of(Email email, UserName name, Password password) {
        return new User(email, new Profile(name), password);
    }

    private User(Email email, Profile profile, Password password) {
        this.email = email;
        this.profile = profile;
        this.password = password;
    }

    protected User() {
    }

    public Article writeArticle(ArticleContents contents) {
        return new Article(this, contents);
    }

    public Article updateArticle(Article article, ArticleUpdateRequest request) {
        if (article.getAuthor() != this) {
            throw new IllegalAccessError("Not authorized to update this article");
        }
        article.updateArticle(request);
        return article;
    }

    public Comment writeCommentToArticle(Article article, String body) {
        return article.addComment(this, body);
    }

    public Article favoriteArticle(Article articleToFavorite) {
        articleFavorited.add(articleToFavorite);
        return articleToFavorite.afterUserFavoritesArticle(this);
    }

    public Article unfavoriteArticle(Article articleToUnfavorite) {
        articleFavorited.remove(articleToUnfavorite);
        return articleToUnfavorite.afterUserUnFavoritesArticle(this);
    }

    User followUser(User followee) {
        followingUsers.add(followee);
        return this;
    }

    User unfollowUser(User followee) {
        followingUsers.remove(followee);
        return this;
    }

    public void deleteArticleComment(Article article, long commentId) {
        article.removeCommentByUser(this, commentId);
    }

    public Set<Comment> viewArticleComments(Article article) {
        return article.getComments().stream()
                .map(this::viewComment)
                .collect(toSet());
    }

    Comment viewComment(Comment comment) {
        viewProfile(comment.getAuthor());
        return comment;
    }

    Profile viewProfile(User user) {
        return user.profile.withFollowing(followingUsers.contains(user));
    }

    public Profile getProfile() {
        return profile;
    }

    boolean matchesPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return password.matchesPassword(rawPassword, passwordEncoder);
    }

    void changeEmail(Email email) {
        this.email = email;
    }

    void changePassword(Password password) {
        this.password = password;
    }

    void changeName(UserName userName) {
        profile.changeUserName(userName);
    }

    void changeBio(String bio) {
        profile.changeBio(bio);
    }

    void changeImage(Image image) {
        profile.changeImage(image);
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public UserName getName() {
        return profile.getUserName();
    }

    String getBio() {
        return profile.getBio();
    }

    Image getImage() {
        return profile.getImage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
