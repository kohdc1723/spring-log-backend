package org.example.springlogbackend.util;

import jakarta.servlet.http.HttpServletResponse;
import org.example.springlogbackend.dto.ApiResponse;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SecurityResponseUtil {
    public static void sendError(
            HttpServletResponse response,
            ErrorCode errorCode,
            ObjectMapper objectMapper
    ) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(errorCode)));
    }

    public static <T> void sendSuccess(
            HttpServletResponse response,
            T data,
            ObjectMapper objectMapper
    ) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(data)));
    }
}
