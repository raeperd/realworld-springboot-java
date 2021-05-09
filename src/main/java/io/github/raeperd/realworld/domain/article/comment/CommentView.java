package io.github.raeperd.realworld.domain.article.comment;

import io.github.raeperd.realworld.domain.user.Profile;

import java.time.LocalDateTime;

public class CommentView {

    private final long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String body;
    private final Profile authorProfile;

    static CommentView of(Comment comment, Profile authorProfile) {
        return new CommentView(comment.getId(), comment.getCreatedAt(),
                comment.getUpdatedAt(), comment.getBody(),
                authorProfile);
    }

    private CommentView(long id, LocalDateTime createdAt, LocalDateTime updatedAt, String body, Profile authorProfile) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = body;
        this.authorProfile = authorProfile;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getBody() {
        return body;
    }

    public Profile getAuthorProfile() {
        return authorProfile;
    }
}
