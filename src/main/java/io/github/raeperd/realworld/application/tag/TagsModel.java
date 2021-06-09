package io.github.raeperd.realworld.application.tag;

import lombok.Getter;

import java.util.Set;

@Getter
class TagsModel {

    private final Set<String> tags;

    TagsModel(Set<String> tags) {
        this.tags = tags;
    }
}
