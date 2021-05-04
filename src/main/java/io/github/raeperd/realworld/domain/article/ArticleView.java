package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.user.profile.Profile;

public class ArticleView {

    private final Article article;
    private final Profile authorProfile;
    private final boolean favorited;
    private final int favoritedCount;

    static ArticleView of(Article article, Profile authorProfile, boolean favorited, int favoritedCount) {
        return new ArticleView(article, authorProfile, favorited, favoritedCount);
    }

    private ArticleView(Article article, Profile authorProfile, boolean favorited, int favoritedCount) {
        this.article = article;
        this.authorProfile = authorProfile;
        this.favorited = favorited;
        this.favoritedCount = favoritedCount;
    }

    public Article getArticle() {
        return article;
    }

    public Profile getAuthorProfile() {
        return authorProfile;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public int getFavoritedCount() {
        return favoritedCount;
    }

}
