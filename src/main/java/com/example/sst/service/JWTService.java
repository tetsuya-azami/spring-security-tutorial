package com.example.sst.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.sst.domain.authentication.AuthenticatedUser;
import com.example.sst.domain.authentication.Role;
import com.example.sst.exception.TokenAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JWTService {
    private static final String EMAIL = "email";
    private static final String ROLES_CLAIM = "roles";
    private static final String ISSUER = "test-issuer";
    private final Algorithm algorithm = Algorithm.HMAC256("secret");

    public AuthenticatedUser resolveJWTToken(String token) throws TokenAuthenticationException {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            String email = decodedJWT.getClaim(EMAIL).asString();
            List<Role> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(Role.class);
            if (username == null || email == null || roles == null) {
                log.info("JWT token is missing required claims");
                throw new TokenAuthenticationException("認証情報が正しくありません。");
            }

            String issuer = decodedJWT.getIssuer();
            if (issuer == null || !issuer.equals(ISSUER)) {
                log.info("JWT token issuer is invalid");
                throw new TokenAuthenticationException("認証情報が正しくありません。");
            }

            Optional<AuthenticatedUser> authenticatedUserOpt = AuthenticatedUser.create(username, email, roles);
            if (authenticatedUserOpt.isEmpty()) {
                throw new TokenAuthenticationException("認証情報が正しくありません。");
            }

            return authenticatedUserOpt.get();
        } catch (JWTVerificationException exception) {
            log.info("JWTの検証に失敗しました。 message:{}, stacktrace: {}", exception.getMessage(), exception.getStackTrace());
            throw new TokenAuthenticationException("認証情報が正しくありません。");
        }
    }

    public String createJWTToken(AuthenticatedUser user) throws TokenAuthenticationException {
        if (user == null) {
            log.info("ユーザ情報がありません。");
            throw new TokenAuthenticationException("認証情報が正しくありません。");
        }

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(3600); // 1 hour expiration

        return JWT.create()
                .withSubject(user.name())
                .withClaim(EMAIL, user.email())
                .withClaim(ROLES_CLAIM, user.roles().stream().map(Role::name).toList())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(ISSUER)
                .sign(algorithm);
    }
}
