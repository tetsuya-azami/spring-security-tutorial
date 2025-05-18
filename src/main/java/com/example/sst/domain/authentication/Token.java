package com.example.sst.domain.authentication;


import org.springframework.util.StringUtils;

import java.util.UUID;

public class Token {
    private final String value;

    private Token(String value) {
        this.value = value;
    }

    public static Token reconstruct(String value) {
        if (StringUtils.hasText(value)) {
            throw new IllegalArgumentException("トークンの値は空ではいけません");
        }
        return new Token(value);
    }

    public static Token create() {
        return new Token(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }
}
