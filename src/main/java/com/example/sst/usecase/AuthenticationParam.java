package com.example.sst.usecase;

import java.util.ArrayList;
import java.util.List;

public record AuthenticationParam(String email, String password) {
    public static Result create(String email, String password) {
        List<String> errorMessages = new ArrayList<>();
        if (email == null || email.isBlank()) {
            errorMessages.add("emailは空ではいけません。");
        }
        if (password == null || password.isBlank()) {
            errorMessages.add("passwordは空ではいけません。");
        }

        if (!errorMessages.isEmpty()) {
            return new Result.Failure(errorMessages);
        }

        return new Result.Success(new AuthenticationParam(email, password));
    }

    public sealed interface Result {
        record Success(AuthenticationParam param) implements Result {
        }

        record Failure(List<String> messages) implements Result {
        }
    }
}
