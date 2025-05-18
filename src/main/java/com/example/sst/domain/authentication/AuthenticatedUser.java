package com.example.sst.domain.authentication;

import java.util.List;

public record AuthenticatedUser(String name, String email, List<Role> roles) {
}
