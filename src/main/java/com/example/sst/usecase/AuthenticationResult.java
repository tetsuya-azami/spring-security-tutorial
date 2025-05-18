package com.example.sst.usecase;

import com.example.sst.domain.authentication.OpaqueToken;

public sealed interface AuthenticationResult {
    record Success(OpaqueToken opaqueToken) implements AuthenticationResult {
    }

    record Failure(AuthenticationErrorDetailCode detailCode, String message) implements AuthenticationResult {
    }
}
