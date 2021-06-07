package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.tag.TagService;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserFindService;
import io.github.raeperd.realworld.domain.user.UserName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.data.util.Optionals.mapIfAllPresent;

@Service
public class ArticleService implements ArticleFindService {

    private final UserFindService userFindService;
    private final TagService tagService;
    private final ArticleRepository articleRepository;

    ArticleService(UserFindService userFindService, TagService tagService, ArticleRepository articleRepository) {
        this.userFindService = userFindService;
        this.tagService = tagService;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Article createNewArticle(long authorId, ArticleContents contents) {
        final var tagsReloaded = tagService.reloadAllTagsIfAlreadyPresent(contents.getTags());
        contents.setTags(tagsReloaded);
        return userFindService.findById(authorId)
                .map(author -> author.writeArticle(contents))
                .map(articleRepository::save)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Article> getFeedByUserId(long userId, Pageable pageable) {
        return userFindService.findById(userId)
                .map(user -> articleRepository.findAllByUserFavoritedContains(user, pageable)
                        .map(article -> article.updateFavoriteByUser(user)))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticleFavoritedByUsername(UserName username, Pageable pageable) {
        return userFindService.findByUsername(username)
                .map(user -> articleRepository.findAllByUserFavoritedContains(user, pageable)
                        .map(article -> article.updateFavoriteByUser(user)))
                .orElse(Page.empty());
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticlesByAuthorName(String authorName, Pageable pageable) {
        return articleRepository.findAllByAuthorProfileUserName(new UserName(authorName), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticlesByTag(String tagValue, Pageable pageable) {
        return tagService.findByValue(tagValue)
                .map(tag -> articleRepository.findAllByContentsTagsContains(tag, pageable))
                .orElse(Page.empty());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Article> getArticleBySlug(String slug) {
        return articleRepository.findFirstByContentsTitleSlug(slug);
    }

    @Transactional
    public Article updateArticle(long userId, String slug, ArticleUpdateRequest request) {
        return mapIfAllPresent(userFindService.findById(userId), getArticleBySlug(slug),
                (user, article) -> user.updateArticle(article, request))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Article favoriteArticle(long userId, String articleSlugToFavorite) {
        return mapIfAllPresent(
                userFindService.findById(userId), getArticleBySlug(articleSlugToFavorite),
                User::favoriteArticle)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Article unfavoriteArticle(long userId, String articleSlugToUnFavorite) {
        return mapIfAllPresent(
                userFindService.findById(userId), getArticleBySlug(articleSlugToUnFavorite),
                User::unfavoriteArticle)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void deleteArticleBySlug(long userId, String slug) {
        userFindService.findById(userId)
                .ifPresentOrElse(user -> articleRepository.deleteArticleByAuthorAndContentsTitleSlug(user, slug),
                        () -> {throw new NoSuchElementException();});
    }
}
