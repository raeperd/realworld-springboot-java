package io.github.raeperd.realworld.application.article.comment;

import io.github.raeperd.realworld.application.article.comment.SingleCommentResponseDTO.CommentResponseDTO;
import io.github.raeperd.realworld.domain.article.comment.CommentView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public class MultipleCommentResponseDTO {

    private final List<CommentResponseDTO> comments;

    static MultipleCommentResponseDTO fromCommentViews(Collection<CommentView> commentViews) {
        return new MultipleCommentResponseDTO(
                commentViews.stream()
                        .map(CommentResponseDTO::fromCommentView)
                        .collect(toList()));
    }
}
