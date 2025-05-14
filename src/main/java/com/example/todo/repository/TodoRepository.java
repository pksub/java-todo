package com.example.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.entity.TodoEntity;
import com.example.todo.entity.UserEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    List<TodoEntity> findByUser(UserEntity userEntity);
    Optional<TodoEntity> findByIdAndUser(Long id, UserEntity userEntity);

    List<TodoEntity> findByTitleContainingOrDescriptionContainingAndUser(String keyword, String keyword1, UserEntity userEntity);

    Optional<Object> findTodoByIdAndUser(Long id, UserEntity user);

    List<TodoEntity> findTodoByUserId(Long id);

    Optional<TodoEntity> findByIdAndUserId(long l, Long id);
}
