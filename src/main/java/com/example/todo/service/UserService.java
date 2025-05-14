package com.example.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.todo.dto.UserDTO;
import com.example.todo.entity.UserEntity;
import com.example.todo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserEntity joinProcess(UserDTO userDTO) {

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        UserEntity data = new UserEntity();

        if (username == null || username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 이름은 필수 입력 항목입니다.");
        }

        if (password == null || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 필수 입력 항목입니다.");
        }

        UserEntity existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 ID 입니다.");
        }

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");

        return userRepository.save(data);
    }

    public boolean authenticate(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 ID");
        }

        // 입력된 비밀번호와 저장된 비밀번호를 비교
        return bCryptPasswordEncoder.matches(password, userEntity.getPassword());
    }

    // 사용자의 정보 업데이트
    @Transactional
    public UserEntity updateUser(String username, UserDTO updatedUser) {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 ID");
        }

        if (updatedUser.getRole() != null ) {
            user.setRole(updatedUser.getRole());
        }

        return userRepository.saveAndFlush(user);
    }

    // 사용자의 삭제
    @Transactional
    public void deleteUser(String username) {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "User not found");
        }
        userRepository.delete(user);
    }

    public UserEntity getUserDetail(String username) {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "User not found");
        }

        return user;
    }
}