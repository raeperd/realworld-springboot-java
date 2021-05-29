package io.github.raeperd.realworld.domain.article.tag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

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
}
