package io.github.raeperd.realworld.application;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@JsonTypeName("errors")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
@RequiredArgsConstructor
public class ErrorResponseDTO {

    private final List<String> body;

    static ErrorResponseDTO fromException(Exception exception) {
        return ofNullable(exception.getMessage())
                .map(message -> new ErrorResponseDTO(singletonList(message)))
                .orElseGet(() -> new ErrorResponseDTO(singletonList(exception.getClass().getName())));
    }
}
