package com.example.sst.usecase;

import com.example.sst.domain.authentication.Token;

public sealed interface AuthenticationResult {
    record Success(Token token) implements AuthenticationResult {
    }

    record Failure(AuthenticationErrorDetailCode detailCode, String message) implements AuthenticationResult {
    }
}
