package com.example.sst.exception.handler;

import com.example.sst.common.AuthenticationErrorResponse;
import com.example.sst.lib.JsonResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationAccessDeniedHandler implements AccessDeniedHandler {
    private final JsonResponseWriter jsonResponseWriter;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        log.info("Access Denied, message:{}, stacktrace: {}", accessDeniedException.getMessage(), accessDeniedException.getStackTrace());

        AuthenticationErrorResponse responseBody = new AuthenticationErrorResponse(
                "AUTHORIZATION_ERROR",
                "このリソースにアクセスする権限がありません"
        );

        jsonResponseWriter.writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, responseBody);
    }
}
