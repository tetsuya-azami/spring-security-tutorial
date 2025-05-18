package com.example.sst.presentation.authentication;

import com.example.sst.usecase.AuthUsecase;
import com.example.sst.usecase.AuthenticationParam;
import com.example.sst.usecase.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthUsecase authUsecase;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("Connection Log: {}", request);

        AuthenticationParam.Result paramResult = AuthenticationParam.create(request.email(), request.password());

        if (paramResult instanceof AuthenticationParam.Result.Failure failure) {
            log.info("Login Param Failure: {}", failure);
            return new LoginResponse.Failure("INVALID_REQUEST", failure.messages().toString());
        }

        AuthenticationResult result = authUsecase.login(((AuthenticationParam.Result.Success) paramResult).param());
        switch (result) {
            case AuthenticationResult.Success success -> {
                log.info("Login Success!");
                return new LoginResponse.Success(success.token().value());
            }
            case AuthenticationResult.Failure failure -> {
                log.info("Login Failure: {}", failure);
                return new LoginResponse.Failure(failure.detailCode().name(), failure.message());
            }
        }
    }
}