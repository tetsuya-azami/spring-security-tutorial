package com.example.sst.exception.handler;

import com.example.sst.common.AuthenticationErrorResponse;
import com.example.sst.lib.JsonResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final JsonResponseWriter jsonResponseWriter;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        log.info("Authentication failed, message:{}, stacktrace: {}", authException.getMessage(), authException.getStackTrace());

        AuthenticationErrorResponse responseBody = new AuthenticationErrorResponse(
                "AUTHENTICATION_ERROR",
                "認証情報が正しくありません"
        );

        jsonResponseWriter.writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, responseBody);
    }
}
