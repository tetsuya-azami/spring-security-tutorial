package com.example.sst.domain.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public record APIKeyAuthentication(
        AuthenticatedUser authUser, boolean authenticated, String apiKey) implements Authentication {
    public static APIKeyAuthentication unauthenticated(String apiKey) {
        return new APIKeyAuthentication(null, false, apiKey);
    }

    public static APIKeyAuthentication authenticated(AuthenticatedUser authUser) {
        return new APIKeyAuthentication(authUser, true, null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.roles()
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authUser;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return null;
    }
}
