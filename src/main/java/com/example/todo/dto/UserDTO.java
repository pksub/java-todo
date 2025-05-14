package com.example.todo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private String username;
    private String password;
    private String role;
}