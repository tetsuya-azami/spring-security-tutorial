package com.example.sst.domain.authentication.factory;

import com.example.sst.common.AuthConstants;
import com.example.sst.domain.authentication.APIKeyAuthentication;
import com.example.sst.domain.authentication.JwtAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationFactory {
    public Optional<Authentication> getAuthenticationFromRequest(HttpServletRequest request) {
        String apiKey = request.getHeader(AuthConstants.API_KEY_AUTHORIZATION_HEADER);
        if (apiKey != null) {
            return Optional.of(APIKeyAuthentication.unauthenticated(apiKey));
        }
        
        String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        if (authHeader != null) {
            String token = stripBearer(authHeader);
            return Optional.of(JwtAuthentication.unauthenticated(token));
        }

        return Optional.empty();
    }

    private String stripBearer(String authHeader) {
        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
}
