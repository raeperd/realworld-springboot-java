package io.github.raeperd.realworld.domain.jwt.exception;

public class InvalidJWTException extends RuntimeException {
    public InvalidJWTException(String message) {
        super(message);
    }
}
