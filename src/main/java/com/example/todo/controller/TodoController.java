package com.example.todo.controller;

import com.example.todo.dto.TodoDTO;
import com.example.todo.entity.TodoEntity;
import com.example.todo.entity.UserEntity;
import com.example.todo.jwt.SecurityUtil;
import com.example.todo.service.TodoService;
import com.example.todo.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;

    public TodoController(TodoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    // Todo 생성
    @PostMapping
    public ResponseEntity<TodoEntity> createTodo(@RequestBody TodoDTO dto) {
        String username = SecurityUtil.getUsername();
        UserEntity user = userService.getUserDetail(username);
        return ResponseEntity.ok(todoService.createTodo(dto, user));
    }

    // 로그인한 사용자의 Todo 전체 조회
    @GetMapping
    public ResponseEntity<List<TodoEntity>> getAllTodos() {
        String username = SecurityUtil.getUsername();
        UserEntity user = userService.getUserDetail(username);

        return ResponseEntity.ok(todoService.getTodosByUser(user));
    }

    // 특정 Todo 조회 (로그인한 사용자 기준)
    @GetMapping("/{id}")
    public ResponseEntity<TodoEntity> getTodo(@PathVariable Long id) {
        String username = SecurityUtil.getUsername();
        UserEntity user = userService.getUserDetail(username);

        TodoEntity todo = todoService.getTodoByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        return ResponseEntity.ok(todo);
    }

    // Todo 수정 (로그인한 사용자 기준)
    @PutMapping("/{id}")
    public ResponseEntity<TodoEntity> updateTodo(@PathVariable Long id,
                                           @RequestBody TodoDTO dto) {
        String username = SecurityUtil.getUsername();
        UserEntity user = userService.getUserDetail(username);
        return ResponseEntity.ok(todoService.updateTodo(id, dto, user));
    }

    // Todo 삭제 (로그인한 사용자 기준)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        String username = SecurityUtil.getUsername();
        UserEntity user = userService.getUserDetail(username);
        todoService.deleteTodo(id, user);
        return ResponseEntity.noContent().build();
    }

    // 키워드로 Todo 검색 (로그인한 사용자 기준)
    @GetMapping("/search")
    public ResponseEntity<List<TodoEntity>> searchTodos(@RequestParam String keyword) {
        String username = SecurityUtil.getUsername();
        UserEntity user = userService.getUserDetail(username);
        return ResponseEntity.ok(todoService.searchTodosByKeywordAndUser(keyword, user));
    }
}
