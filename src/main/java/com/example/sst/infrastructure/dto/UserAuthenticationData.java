package com.example.sst.infrastructure.dto;

import lombok.Data;

@Data
public class UserAuthenticationData {
    private String userName;
    private String email;
    private String password;
    private String roleName;
}
