package io.github.raeperd.realworld.domain.article.tag;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface TagRepository extends Repository<Tag, Long> {

    List<Tag> findAll();

    Optional<Tag> findFirstByValue(String value);
}
