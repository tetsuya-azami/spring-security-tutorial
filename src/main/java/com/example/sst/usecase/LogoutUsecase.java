package com.example.sst.usecase;

import com.example.sst.domain.authentication.OpaqueToken;
import com.example.sst.infrastructure.repository.AuthUserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutUsecase {
    private final AuthUserCacheRepository authUserCacheRepository;

    public void logout(String token) {
        authUserCacheRepository.delete(OpaqueToken.reconstruct(token));
    }
}
