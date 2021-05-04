package io.github.raeperd.realworld.application.article.tag;

import io.github.raeperd.realworld.domain.article.tag.Tag;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class TagsResponseDTO {

    private final List<String> tags;

    public TagsResponseDTO(List<Tag> tags) {
        this.tags = tags.stream().map(Tag::getName).collect(toList());
    }
}
