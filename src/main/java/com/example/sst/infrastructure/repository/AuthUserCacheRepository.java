package com.example.sst.infrastructure.repository;

import com.example.sst.domain.authentication.AuthenticatedUser;
import com.example.sst.domain.authentication.Token;
import com.example.sst.domain.authentication.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// TODO: change to use redis
@Repository
public class AuthUserCacheRepository {
    private static final Map<Token, Authentication> authUserMap = new ConcurrentHashMap<>();

    public void save(Token token, AuthenticatedUser authenticatedUser) {
        if (token == null || authenticatedUser == null) {
            throw new IllegalArgumentException("トークンおよび認証ユーザはnullではいけません");
        }
        if (authUserMap.containsKey(token)) {
            return;
        }
        
        authUserMap.put(token, new UserAuthentication(authenticatedUser));
    }

    public Optional<Authentication> get(Token token) {
        if (token == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(authUserMap.get(token));
    }

    public void delete(Token token) {
        authUserMap.remove(token);
    }
}
