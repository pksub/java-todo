package com.example.todo.repository;

import com.example.todo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsername(String username);

    Optional<UserEntity> findAllByUsername(String username);

    UserEntity findByUsername(String username);
}