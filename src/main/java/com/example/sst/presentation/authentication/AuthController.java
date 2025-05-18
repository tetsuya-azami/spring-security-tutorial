package com.example.sst.presentation.authentication;

import com.example.sst.exception.TokenAuthenticationException;
import com.example.sst.usecase.AuthenticationParam;
import com.example.sst.usecase.AuthenticationResult;
import com.example.sst.usecase.LoginUsecase;
import com.example.sst.usecase.LogoutUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final LoginUsecase loginUsecase;
    private final LogoutUsecase logoutUsecase;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Connection Log: {}", request);

        AuthenticationParam.Result paramResult = AuthenticationParam.create(request.email(), request.password());

        if (paramResult instanceof AuthenticationParam.Result.Failure failure) {
            log.info("Login Param Failure: {}", failure);
            return ResponseEntity.badRequest()
                    .body(new LoginResponse.Failure("INVALID_REQUEST", failure.messages().toString()));
        }

        AuthenticationResult result = loginUsecase.execute(((AuthenticationParam.Result.Success) paramResult).param());

        if (result instanceof AuthenticationResult.Failure failure) {
            log.info("Login Failure: {}", failure);
            switch (failure.detailCode()) {
                case USER_NOT_FOUND -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new LoginResponse.Failure(failure.detailCode().name(), failure.message()));
                }
                case ROLE_INVALID -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new LoginResponse.Failure(failure.detailCode().name(), failure.message()));
                }
            }
        }

        log.info("Login Success!");
        return ResponseEntity.ok(new LoginResponse.Success(((AuthenticationResult.Success) result).opaqueToken().value()));
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/loginWithJWT")
    public ResponseEntity<LoginResponse> loginWithJWT(@RequestBody LoginRequest request) throws TokenAuthenticationException {
        log.info("Connection Log: {}", request);

        AuthenticationParam.Result paramResult = AuthenticationParam.create(request.email(), request.password());

        if (paramResult instanceof AuthenticationParam.Result.Failure failure) {
            log.info("Login Param Failure: {}", failure);
            return ResponseEntity.badRequest()
                    .body(new LoginResponse.Failure("INVALID_REQUEST", failure.messages().toString()));
        }

        String JWTToken = loginUsecase.executeJWTLogin(((AuthenticationParam.Result.Success) paramResult).param());

        log.info("Login Success!");
        return ResponseEntity.ok(new LoginResponse.Success(JWTToken));
    }

    @GetMapping("/process")
    public String process() {
        return "process";
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        logoutUsecase.logout(token);
        return "logout";
    }
}