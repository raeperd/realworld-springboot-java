package io.github.raeperd.realworld.domain.article;

import java.util.Optional;

public interface ArticleFindService {

    Optional<Article> getArticleBySlug(String slug);
}
