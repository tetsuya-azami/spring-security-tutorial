package com.example.sst.domain.authentication;

import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    public static Optional<Role> fromString(String str) {
        try {
            return Optional.of(Role.valueOf(str));
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}
