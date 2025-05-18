package com.example.sst.filter;

import com.example.sst.common.AuthConstants;
import com.example.sst.domain.authentication.OpaqueToken;
import com.example.sst.exception.TokenAuthenticationException;
import com.example.sst.infrastructure.repository.AuthUserCacheRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityAuthenticationFilter extends OncePerRequestFilter {
    private final AuthUserCacheRepository authUserCacheRepository;

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

        Optional<Authentication> authenticatedUserOpt = authUserCacheRepository.get(OpaqueToken.reconstruct(authorizationToken));
        if (authenticatedUserOpt.isEmpty()) {
            throw new TokenAuthenticationException("認証情報が不正です。");
        }

        Authentication authentication = authenticatedUserOpt.get();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        filterChain.doFilter(request, response);
    }
}
