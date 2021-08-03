package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.tag.Tag;
import io.github.raeperd.realworld.domain.user.User;
import io.github.raeperd.realworld.domain.user.UserName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends Repository<Article, Long> {

    Article save(Article article);

    Page<Article> findAll(Pageable pageable);
    Page<Article> findAllByUserFavoritedContains(User user, Pageable pageable);
    Page<Article> findAllByAuthorProfileUserName(UserName authorName, Pageable pageable);
    Page<Article> findAllByContentsTagsContains(Tag tag, Pageable pageable);
    Optional<Article> findFirstByContentsTitleSlug(String slug);

    @Query(value = "select a.title from articles a where author_id = :author", nativeQuery = true)
    List<String> findAllByAuthor(long author);
    @Query(value = "select * from articles a where a.title = :title", nativeQuery = true)
    Optional<Article> findByTitle(String title);

    void deleteArticleByAuthorAndContentsTitleSlug(User author, String slug);

}
