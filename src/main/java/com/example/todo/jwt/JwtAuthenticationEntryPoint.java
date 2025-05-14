package com.example.todo.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        if (authException.getCause() instanceof UsernameNotFoundException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "NOT FOUND");

        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: JWT token is missing or invalid");

        }
    }
}