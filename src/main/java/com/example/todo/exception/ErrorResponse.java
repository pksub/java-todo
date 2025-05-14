package com.example.todo.exception;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private  int errorCode;
    private  String message;

    public ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
//
//    private void sendErrorResponse(HttpServletResponse response, int errorCode, String message) throws IOException {
//        response.setStatus(errorCode);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        String json = String.format("{\"errorCode\": %d, \"message\": \"%s\"}", errorCode, message);
//        response.getWriter().write(json);
//    }

} 