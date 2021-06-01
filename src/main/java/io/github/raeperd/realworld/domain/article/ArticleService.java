package io.github.raeperd.realworld.domain.article;

import io.github.raeperd.realworld.domain.article.tag.TagService;
import io.github.raeperd.realworld.domain.user.UserFindService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ArticleService {

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
}
