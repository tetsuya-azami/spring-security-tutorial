package com.example.sst.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JsonResponseWriter {
    private final ObjectMapper objectMapper;

    public void writeJsonResponse(HttpServletResponse response, int httpStatusCode, Object responseBody) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatusCode);
        objectMapper.writeValue(response.getOutputStream(), responseBody);
    }
}
