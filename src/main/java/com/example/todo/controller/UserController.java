package com.example.todo.controller;

import com.example.todo.jwt.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.todo.dto.UserDTO;
import com.example.todo.entity.UserEntity;
import com.example.todo.jwt.JWTUtil;
import com.example.todo.jwt.SecurityUtil;
import com.example.todo.service.UserService;
import org.springframework.web.server.ResponseStatusException;

@Controller
@ResponseBody
@RequestMapping("/users")
public class UserController {

     private static final Logger logger = LoggerFactory.getLogger(UserController.class);

     
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {

        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?>  joinProcess(@RequestBody UserDTO userDTO) {

        System.out.println(userDTO.getUsername());
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        UserEntity save = userService.joinProcess(userDTO);

        return ResponseEntity.ok(save);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginProcess(UserDTO userDTO) {
        boolean isAuthenticated = userService.authenticate(userDTO.getUsername(), userDTO.getPassword());
        if (isAuthenticated) {
            String role = SecurityUtil.getRole();
            String token = jwtUtil.createJwt(userDTO.getUsername(), role, 60*60*1000L );
            JwtToken jwtToken = JwtToken.builder()
                    .grantType("Bearer")
                    .accessToken(token)
                    .refreshToken("")
                    .build();
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
    // GET /users/me -> 로그인된 사용자의 정보 가져오기
    @GetMapping("/me")
    public ResponseEntity<UserEntity> getCurrentUser() {

        // AuthenticationPrincipal을 통해 현재 로그인된 사용자 정보를 받아옴
        String username = SecurityUtil.getUsername();
        UserEntity user = userService.getUserDetail(username);
        return ResponseEntity.ok(user);
    }

    // PUT /users/me -> 로그인된 사용자의 정보 수정
    @PutMapping("/me")
    public ResponseEntity<UserEntity> updateCurrentUser( @RequestBody UserDTO updatedUser) {
        // 로그인된 사용자의 정보를 수정
        logger.info("PUT /users/me 요청 시작");
        if (updatedUser == null) {
            logger.error("요청된 UserDTO가 null입니다.");
            return ResponseEntity.badRequest().build();
        }
        String username = SecurityUtil.getUsername();
        logger.info("현재 사용자 이름: {}", username);

        UserEntity updated = userService.updateUser(username, updatedUser);
        logger.info("사용자 정보 업데이트 완료: {}", updated);

        return ResponseEntity.ok(updated);
    }

    // DELETE /users/me -> 로그인된 사용자 삭제
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        String username = SecurityUtil.getUsername();

        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}