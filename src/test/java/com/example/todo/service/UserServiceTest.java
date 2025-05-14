package com.example.todo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.todo.dto.UserDTO;
import com.example.todo.entity.UserEntity;
import com.example.todo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setRole("ROLE_ADMIN");
    }

    @Test
    void joinProcess() {
        log.info("테스트 시작: joinProcess");

        // given
        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole("ROLE_ADMIN");

        given(userRepository.findByUsername("testuser")).willReturn(null);
        given(userRepository.save(any(UserEntity.class))).willReturn(savedUser);

        // when
        UserEntity result = userService.joinProcess(userDTO);

        // 디버깅
        System.out.println("Result: " + result);

        // then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("ROLE_ADMIN", result.getRole());
        verify(userRepository).save(any(UserEntity.class));

        log.info("테스트 끝: joinProcess");
    }

    @Test
    void updateUser() {
        log.info("테스트 시작: updateUser");

        // given
        UserEntity user = new UserEntity();
        user.setUsername("testuser");

        UserDTO dto = new UserDTO();
        dto.setUsername("testuser");
        dto.setRole("ROLE_USER");

        given(userRepository.findByUsername("testuser")).willReturn(user);

        // when
        userService.updateUser("testuser", dto);

        // then
        verify(userRepository).saveAndFlush(user);

        log.info("테스트 끝: updateUser");
    }

    @Test
    void deleteUser() {
        log.info("테스트 시작: deleteUser");

        // given
        UserEntity user = new UserEntity();
        user.setUsername("testuser");

        given(userRepository.findByUsername("testuser")).willReturn(user);

        // when
        userService.deleteUser("testuser");

        // then
        verify(userRepository).delete(user);

        log.info("테스트 끝: deleteUser");
    }

    @Test
    void getUserDetail() {
        log.info("테스트 시작: getUserDetail");

        // given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testuser");

        given(userRepository.findByUsername("testuser")).willReturn(user);

        // when
        UserEntity result = userService.getUserDetail("testuser");

        // then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");

        log.info("테스트 끝: getUserDetail");
    }
}