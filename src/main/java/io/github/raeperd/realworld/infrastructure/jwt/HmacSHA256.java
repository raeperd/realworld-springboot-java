package io.github.raeperd.realworld.infrastructure.jwt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

class HmacSHA256 {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private HmacSHA256() {
    }

    public static byte[] sign(byte[] secret, String message) {
        try {
            final var hmacSHA256 = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            hmacSHA256.init(new SecretKeySpec(secret, HMAC_SHA256_ALGORITHM));
            return hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new HmacSHA256SignFailedException(exception);
        }
    }

    private static class HmacSHA256SignFailedException extends RuntimeException {
        public HmacSHA256SignFailedException(Throwable cause) {
            super(cause);
        }
    }

}
