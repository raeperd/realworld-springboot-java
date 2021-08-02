package io.github.raeperd.realworld.infrastructure.handler;

import io.github.raeperd.realworld.domain.article.comment.reports.ReportConstants;
import io.github.raeperd.realworld.domain.article.comment.reports.ReportException;
import io.github.raeperd.realworld.infrastructure.error.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({ReportException.class})
    public ResponseEntity<ApiError> reportException(final HttpServletRequest req, final ReportException exception) {
        Map<String,String> error = new HashMap<>();
        error.put("message",exception.getMessage());
        if(exception.getMessage().equals(ReportConstants.USER_NOT_ALLOWED)) {
            return new ResponseEntity<>(new ApiError(error, HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(new ApiError(error, HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value()),HttpStatus.NOT_FOUND);
        }
    }
}

