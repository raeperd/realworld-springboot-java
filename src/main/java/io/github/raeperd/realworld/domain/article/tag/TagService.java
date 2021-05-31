package io.github.raeperd.realworld.domain.article.tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toSet;

@Service
public class TagService {

    private final TagRepository tagRepository;

    TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public Set<Tag> findAll() {
        return new HashSet<>(tagRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Set<Tag> reloadAllTagsIfAlreadyPresent(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> tagRepository.findFirstByValue(valueOf(tag)).orElse(tag))
                .collect(toSet());
    }
}
