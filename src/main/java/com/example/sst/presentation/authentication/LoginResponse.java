package com.example.sst.presentation.authentication;

public sealed interface LoginResponse {
    record Success(String token) implements LoginResponse {
    }

    record Failure(String errorDetailCode, String message) implements LoginResponse {
    }
}
