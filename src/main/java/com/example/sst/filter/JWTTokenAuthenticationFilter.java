package com.example.sst.filter;

import com.example.sst.common.AuthConstants;
import com.example.sst.domain.authentication.AuthenticatedUser;
import com.example.sst.domain.authentication.UserAuthentication;
import com.example.sst.infrastructure.repository.AuthUserCacheRepository;
import com.example.sst.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTTokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthUserCacheRepository authUserCacheRepository;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationToken = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authorizationToken)) {
            // anonymous authentication
            filterChain.doFilter(request, response);
            return;
        }

        String JWTToken = stripBearer(authorizationToken);
        AuthenticatedUser authenticatedUser = jwtService.resolveJWTToken(JWTToken);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UserAuthentication(authenticatedUser));
        SecurityContextHolder.setContext(securityContext);
        filterChain.doFilter(request, response);
    }

    private String stripBearer(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
