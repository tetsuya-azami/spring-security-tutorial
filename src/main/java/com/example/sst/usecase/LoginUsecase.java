package com.example.sst.usecase;

import com.example.sst.domain.authentication.AuthenticatedUser;
import com.example.sst.domain.authentication.OpaqueToken;
import com.example.sst.domain.authentication.Role;
import com.example.sst.exception.TokenAuthenticationException;
import com.example.sst.infrastructure.dto.UserAuthenticationData;
import com.example.sst.infrastructure.repository.AuthUserCacheRepository;
import com.example.sst.infrastructure.repository.UserMapper;
import com.example.sst.service.JWTService;
import com.example.sst.service.UserDataRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginUsecase {
    private final UserMapper userMapper;
    private final AuthUserCacheRepository authUserCacheRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDataRegister userDataRegister;
    private final JWTService jwtService;

    public AuthenticationResult execute(AuthenticationParam param) {
        userDataRegister.insertUserIfNotExists();

        // 本来Dataがusecaseに入るのは良くないが、今回は簡易的に実装するためにこのまま
        List<UserAuthenticationData> userDataList = userMapper.selectByEmail(param.email());
        if (userDataList.isEmpty()) {
            return new AuthenticationResult.Failure(AuthenticationErrorDetailCode.USER_NOT_FOUND, "ユーザが見つかりませんでした。");
        }

        UserAuthenticationData userData = userDataList.getFirst();

        List<Optional<Role>> roleOpts = userDataList.stream()
                .map(UserAuthenticationData::getRoleName)
                .map(Role::fromString)
                .toList();

        if (roleOpts.stream().anyMatch(Optional::isEmpty)) {
            return new AuthenticationResult.Failure(AuthenticationErrorDetailCode.ROLE_INVALID, "サーバーエラーです。");
        }
        List<Role> roles = roleOpts.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (!passwordEncoder.matches(param.password(), userData.getPassword())) {
            return new AuthenticationResult.Failure(AuthenticationErrorDetailCode.USER_NOT_FOUND, "ユーザが見つかりませんでした。");
        }

        Optional<AuthenticatedUser> authenticatedUserOpt = AuthenticatedUser.create(
                userData.getUserName(),
                userData.getEmail(),
                roles
        );
        if (authenticatedUserOpt.isEmpty()) {
            return new AuthenticationResult.Failure(AuthenticationErrorDetailCode.USER_NOT_FOUND, "ユーザが見つかりませんでした。");
        }

        OpaqueToken opaqueToken = OpaqueToken.create();
        authUserCacheRepository.save(opaqueToken, authenticatedUserOpt.get());

        return new AuthenticationResult.Success(opaqueToken);
    }

    public String executeJWTLogin(AuthenticationParam param) throws TokenAuthenticationException {
        userDataRegister.insertUserIfNotExists();

        // 本来Dataがusecaseに入るのは良くないが、今回は簡易的に実装するためにこのまま
        List<UserAuthenticationData> userDataList = userMapper.selectByEmail(param.email());
        if (userDataList.isEmpty()) {
            throw new TokenAuthenticationException("ユーザが見つかりませんでした。");
        }

        UserAuthenticationData userData = userDataList.getFirst();

        List<Optional<Role>> roleOpts = userDataList.stream()
                .map(UserAuthenticationData::getRoleName)
                .map(Role::fromString)
                .toList();

        if (roleOpts.stream().anyMatch(Optional::isEmpty)) {
            // マスタデータ不整合でサーバーエラーだが簡易的に実装するためにこのまま
            throw new TokenAuthenticationException("サーバーエラーです。");
        }
        List<Role> roles = roleOpts.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (!passwordEncoder.matches(param.password(), userData.getPassword())) {
            throw new TokenAuthenticationException("ユーザが見つかりませんでした。");
        }

        Optional<AuthenticatedUser> authenticatedUserOpt = AuthenticatedUser.create(
                userData.getUserName(),
                userData.getEmail(),
                roles
        );
        if (authenticatedUserOpt.isEmpty()) {
            throw new TokenAuthenticationException("ユーザが見つかりませんでした。");
        }

        return jwtService.createJWTToken(authenticatedUserOpt.get());
    }
}
