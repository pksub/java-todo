package com.example.todo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    public TodoDTO(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public TodoDTO() {

    }
}
