package io.github.raeperd.realworld.domain.article.comment.reports;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReportException extends RuntimeException {
    public ReportException(String message) {
        super(message);
    }
}
