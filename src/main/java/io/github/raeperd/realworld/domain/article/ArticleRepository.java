package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.tag.Tag;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface ArticleRepository extends Repository<Article, Long> {

    Article save(Article article);

    Page<Article> findAll(Pageable pageable);
    Page<Article> findAllByUserFavoritedContains(User user, Pageable pageable);
    Page<Article> findAllByAuthorProfileUserName(UserName authorName, Pageable pageable);
    Page<Article> findAllByContentsTagsContains(Tag tag, Pageable pageable);
    Optional<Article> findFirstByContentsTitleSlug(String slug);

}
