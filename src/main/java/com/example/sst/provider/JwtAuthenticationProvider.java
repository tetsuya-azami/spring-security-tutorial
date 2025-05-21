package com.example.sst.provider;

import com.example.sst.domain.authentication.AuthenticatedUser;
import com.example.sst.domain.authentication.JwtAuthentication;
import com.example.sst.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JWTService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        AuthenticatedUser authenticatedUser = jwtService.resolveJWTToken(jwtAuthentication.jwtToken());

        return JwtAuthentication.authenticated(authenticatedUser);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }
}