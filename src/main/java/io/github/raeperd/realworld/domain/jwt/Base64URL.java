package io.github.raeperd.realworld.domain.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class Base64URL {

    private Base64URL() {
    }

    public static String encodeFromString(String rawString) {
        return encodeFromBytes(rawString.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeFromBytes(byte[] rawBytes) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(rawBytes);
    }

    public static String decodeFromString(String base64URLString) {
        return new String(Base64.getUrlDecoder()
                .decode(base64URLString));
    }
}
