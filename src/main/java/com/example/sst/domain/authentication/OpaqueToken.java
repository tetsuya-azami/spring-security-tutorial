package com.example.sst.domain.authentication;


import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.util.UUID;

@EqualsAndHashCode
public class OpaqueToken {
    private final String value;

    private OpaqueToken(String value) {
        this.value = value;
    }

    public static OpaqueToken reconstruct(String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("トークンの値は空ではいけません");
        }
        return new OpaqueToken(value);
    }

    public static OpaqueToken create() {
        return new OpaqueToken(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }
}
