package com.example.sst.domain.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class AuthenticatedUser {
    private final String email;
    private final String name;
    private final List<Role> roles;
    private final AuthUserType authUserType;

    private AuthenticatedUser(String name, String email, List<Role> roles, AuthUserType authUserType) {
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.authUserType = authUserType;
    }

    public static Optional<AuthenticatedUser> create(String name, String email, List<Role> roles, AuthUserType authUserType) {
        List<String> errors = new ArrayList<>();
        if (!StringUtils.hasText(name)) {
            errors.add("usernameが空です");
        }
        if (!StringUtils.hasText(email)) {
            errors.add("emailが空です");
        }
        if (roles == null || roles.isEmpty()) {
            errors.add("rolesが空です");
        }
        if (roles != null && roles.stream().anyMatch(Objects::isNull)) {
            errors.add("rolesにnullが含まれています");
        }
        if (authUserType == null) {
            errors.add("authUserTypeがnullです");
        }

        if (!errors.isEmpty()) {
            log.info("ユーザ情報が正しくありません。errors: {}", errors);
            return Optional.empty();
        }

        return Optional.of(new AuthenticatedUser(name, email, roles, authUserType));
    }

    public boolean isInternalUser() {
        return authUserType == AuthUserType.INTERNAL;
    }

    public String email() {
        return email;
    }

    public String name() {
        return name;
    }

    public List<Role> roles() {
        return roles;
    }
}
