package io.github.raeperd.realworld.application.article.tag;

import io.github.raeperd.realworld.domain.article.tag.TagRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tags")
@RestController
public class TagRestController {

    private final TagRepository tagRepository;

    public TagRestController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public TagsResponseDTO getTags() {
        return new TagsResponseDTO(tagRepository.findAll());
    }

}
