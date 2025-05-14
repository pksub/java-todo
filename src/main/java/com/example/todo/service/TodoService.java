package com.example.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.todo.dto.TodoDTO;
import com.example.todo.entity.TodoEntity;
import com.example.todo.entity.UserEntity;
import com.example.todo.repository.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoEntity createTodo(TodoDTO dto, UserEntity userEntity) {

        TodoEntity todo = new TodoEntity();
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setCompleted(dto.isCompleted());
        todo.setUser(userEntity);

        return todoRepository.save(todo);
    }

    @Transactional
    public List<TodoEntity> getTodosByUser(UserEntity user) {
        // List<TodoEntity> todos = todoRepository.findByUser(user);
        return  todoRepository.findTodoByUserId(user.getId());
    }

    public Optional<TodoEntity> getTodoByIdAndUser(Long id, UserEntity user) {
        return todoRepository.findByIdAndUserId(id, user.getId());
    }

    @Transactional
    public TodoEntity updateTodo(Long id, TodoDTO dto, UserEntity user) {
//        TodoEntity todo = todoRepository.findByIdAndUser(id, member)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TodoEntity not found or access denied"));

        TodoEntity todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));


        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setCompleted(dto.isCompleted());

        return todoRepository.save(todo);
    }

    @Transactional
    public void deleteTodo(Long id, UserEntity user) {
        TodoEntity todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " not found or access denied"));
        todoRepository.delete(todo);
    }

    public List<TodoEntity> searchTodosByKeywordAndUser(String keyword, UserEntity user) {
        return todoRepository.findByTitleContainingOrDescriptionContainingAndUser(keyword, keyword, user);
    }

    public TodoEntity findByIdAndUserId(long l, Long id) {
        return todoRepository.findByIdAndUserId(l, id).get();
    }
}
