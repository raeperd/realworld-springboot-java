package io.github.raeperd.realworld.domain.jwt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class HmacSHA256 {

    private static final String ALGORITHM = "HmacSHA256";

    private HmacSHA256() {
    }

    public static byte[] sign(byte[] secret, String message) {
        try {
            final var hmacSHA256 = Mac.getInstance(ALGORITHM);
            hmacSHA256.init(new SecretKeySpec(secret, ALGORITHM));
            return hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | IllegalArgumentException | InvalidKeyException exception) {
            throw new HmacSHA256SignFailedException(exception);
        }
    }

    private static class HmacSHA256SignFailedException extends RuntimeException {
        public HmacSHA256SignFailedException(Throwable cause) {
            super(cause);
        }
    }
}
