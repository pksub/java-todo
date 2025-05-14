package com.example.todo.service;


import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.todo.dto.UserDTO;
import com.example.todo.entity.UserEntity;
import com.example.todo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginTest {

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

    // 회원가입 테스트
    @Test
    void signupTest() {
        log.info("테스트 시작: signupTest");

        // given
        UserDTO loginDto = new UserDTO();

        Random random = new Random();
        loginDto.setUsername("test" + random.nextInt(900));
        loginDto.setPassword("password123");
        loginDto.setRole("ROLE_USER");

        // when
        ResponseEntity<UserEntity> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + randomServerPort + "/users/signup",
                loginDto,
                UserEntity.class
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        log.info("테스트 끝: signupTest");
    }

    // 이미 존재하는 아이디로 가입하려는 테스트
    @Test
    void signupWithExistingUsernameTest() {
        log.info("테스트 시작: signupWithExistingUsernameTest");

        // given
        UserDTO existingUserDto = new UserDTO();
        existingUserDto.setUsername("testuser"); // 이미 존재하는 사용자 이름
        existingUserDto.setPassword("password123");
        existingUserDto.setRole("ROLE_USER");

        // 먼저 사용자 생성
        testRestTemplate.postForEntity(
                "http://localhost:" + randomServerPort + "/users/signup",
                existingUserDto,
                UserEntity.class
        );

        // 동일한 사용자 이름으로 다시 가입 시도
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + randomServerPort + "/users/signup",
                existingUserDto,
                String.class
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // 400 상태 코드 확인
        log.info("테스트 끝: signupWithExistingUsernameTest");
    }

    // 이름 없이 회원가입 테스트
    @Test
    void signupWithoutUsernameTest() {
        log.info("테스트 시작: signupWithoutUsernameTest");

        // given
        UserDTO loginDto = new UserDTO();
        loginDto.setUsername(""); // 이름을 입력하지 않음
        loginDto.setPassword("password123");
        loginDto.setRole("ROLE_USER");

        // when
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + randomServerPort + "/users/signup",
                loginDto,
                String.class
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // 400 상태 코드 확인
        log.info("테스트 끝: signupWithoutUsernameTest");
    }

    // 패스워드 없이 회원가입 테스트
    @Test
    void signupWithoutPasswordTest() {
        log.info("테스트 시작: signupWithoutPasswordTest");

        // given
        UserDTO loginDto = new UserDTO();
        loginDto.setUsername("testuser");
        loginDto.setPassword(""); // 패스워드를 입력하지 않음
        loginDto.setRole("ROLE_USER");

        // when
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + randomServerPort + "/users/signup",
                loginDto,
                String.class
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // 400 상태 코드 확인
        log.info("테스트 끝: signupWithoutPasswordTest");
    }

    // 유효하지 않은 ID login Test
    @Test
    void invalidIdTest() {
        log.info("테스트 시작: invalidIdTest");

        // given
        UserDTO loginDto = new UserDTO();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password123");

        // when
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + randomServerPort + "/users/login",
                loginDto,
                String.class
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        log.info("테스트 끝: invalidIdTest");
    }

    // JWT 토큰 없는 User Get Test
    @Test
    void getCuUserNotJwtTest() {
        log.info("테스트 시작: getCuUserNotJwtTest");

        // given
        String currentUserUrl = "http://localhost:" + randomServerPort + "/users/me";

        // 로그인 후 토큰 발급
        UserDTO loginDto = new UserDTO();
        loginDto.setUsername("test");
        loginDto.setPassword("1234");

        ResponseEntity<String> loginResponse = testRestTemplate.postForEntity(
                "http://localhost:" + randomServerPort + "/users/login",
                loginDto,
                String.class
        );
        log.info("로그인 응답: " + loginResponse);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // when
        ResponseEntity<UserDTO> responseEntity = testRestTemplate.exchange(
                currentUserUrl,
                HttpMethod.GET,
                requestEntity,
                UserDTO.class
        );
        log.info("응답 엔티티: " + responseEntity);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        log.info("테스트 끝: getCuUserNotJwtTest");
    }
}