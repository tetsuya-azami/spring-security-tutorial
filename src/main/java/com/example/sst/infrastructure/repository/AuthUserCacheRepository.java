package com.example.sst.infrastructure.repository;

import com.example.sst.domain.authentication.AuthenticatedUser;
import com.example.sst.domain.authentication.OpaqueToken;
import com.example.sst.domain.authentication.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// TODO: change to use redis
@Repository
public class AuthUserCacheRepository {
    private static final Map<OpaqueToken, Authentication> authUserMap = new ConcurrentHashMap<>();

    public void save(OpaqueToken opaqueToken, AuthenticatedUser authenticatedUser) {
        if (opaqueToken == null || authenticatedUser == null) {
            throw new IllegalArgumentException("トークンおよび認証ユーザはnullではいけません");
        }
        if (authUserMap.containsKey(opaqueToken)) {
            return;
        }

        authUserMap.put(opaqueToken, new UserAuthentication(authenticatedUser));
    }

    public Optional<Authentication> get(OpaqueToken opaqueToken) {
        if (opaqueToken == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(authUserMap.get(opaqueToken));
    }

    public void delete(OpaqueToken opaqueToken) {
        authUserMap.remove(opaqueToken);
    }
}
