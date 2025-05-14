package com.example.todo.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}