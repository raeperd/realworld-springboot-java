package io.github.raeperd.realworld.infrastructure.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class Base64URL {

    private Base64URL() {
    }

    static String base64URLFromString(String rawString) {
        return base64URLFromBytes(rawString.getBytes(StandardCharsets.UTF_8));
    }

    static String base64URLFromBytes(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(bytes);
    }

    static String stringFromBase64URL(String base64URL) {
        return new String(Base64.getUrlDecoder().decode(base64URL));
    }

}
