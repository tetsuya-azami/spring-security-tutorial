package com.example.sst.service;

import com.example.sst.infrastructure.dto.UserAuthenticationData;
import com.example.sst.infrastructure.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDataRegister {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void insertUserIfNotExists() {
        String testDataEmail = "hoge@example.com";
        List<UserAuthenticationData> userAuthenticationData = userMapper.selectByEmail(testDataEmail);
        if (!userAuthenticationData.isEmpty()) {
            return;
        }

        String encodedPassword = passwordEncoder.encode("password");
        userMapper.insertUser("hoge", testDataEmail, encodedPassword, 1);

        userMapper.insertUserRole(testDataEmail, 1);
        userMapper.insertUserRole(testDataEmail, 2);
    }
}
