package com.example.sst.provider;

import com.example.sst.domain.authentication.APIKeyAuthentication;
import com.example.sst.domain.authentication.AuthUserType;
import com.example.sst.domain.authentication.AuthenticatedUser;
import com.example.sst.domain.authentication.Role;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private final Map<String, String> apiKeysToClientIds = new HashMap<>() {
        {
            put("client-a", "abc");
            put("client-b", "111");
        }
    };

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        APIKeyAuthentication apiKeyAuthentication = (APIKeyAuthentication) authentication;
        String apiKey = apiKeyAuthentication.apiKey();

        if (!apiKeysToClientIds.containsKey(apiKey)) {
            throw new BadCredentialsException("API keyが正しくありません");
        }

        String clientId = apiKeysToClientIds.get(apiKey);
        Optional<AuthenticatedUser> authenticatedUserOpt = AuthenticatedUser.create(clientId, clientId, List.of(Role.ROLE_ADMIN), AuthUserType.APPLICATION);
        if (authenticatedUserOpt.isEmpty()) {
            throw new BadCredentialsException("ユーザ情報が正しくありません");
        }
        
        return APIKeyAuthentication.authenticated(authenticatedUserOpt.get());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return APIKeyAuthentication.class.isAssignableFrom(authentication);
    }
}
