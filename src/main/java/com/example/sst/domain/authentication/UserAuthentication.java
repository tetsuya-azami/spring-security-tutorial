package com.example.sst.domain.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserAuthentication(AuthenticatedUser authenticatedUser) implements Authentication {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authenticatedUser.roles();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authenticatedUser;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // No-op
    }

    @Override
    public String getName() {
        return authenticatedUser.name();
    }
}
