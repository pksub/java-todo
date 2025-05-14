# Todo Application

이 프로젝트는 사용자 인증 및 TODO 관리 기능을 제공하는 Spring Boot 기반의 RESTful API 애플리케이션입니다.

---

## 📋 프로젝트 세팅

### 주요 기술 스택
- **Java**: 17
- **Spring Boot**: 3.3.11
- **Gradle**: 7.6
- **Database**: SQLite
- **JWT**: 0.12.3
- **Lombok**: 코드 간소화
- **JUnit 5**: 테스트 프레임워크
- **IDE**: InteliJ IDEA, vscode
- **DB**: SQLite , JPA

---

## ⚙️ 프로젝트 실행 방법


###  **환경 설정**
`src/main/resources/application.properties`

```properties
# 서버 설정
server.port=8080
```

### db init 
`src/main/db/init_db.sql`  - DB 초기화 파일

###  **의존성 설치**
Gradle을 사용하여 의존성을 설치합니다.
```bash
./gradlew build
```

###  **애플리케이션 실행**
```bash
./gradlew bootRun
```

## 📚 API 명세 요약

## 🛠️ 주요 기능
1. **회원가입 및 로그인**:
   - JWT를 사용한 인증 및 권한 관리.
2. **USER 관리**:
   - USER 추가, 조회, 수정, 삭제 기능.
3. **TODO 관리**:
   - TODO 추가, 조회, 수정, 삭제 기능.
4. **예외 처리**:
   - 글로벌 예외 처리 핸들러를 통한 일관된 에러 응답.

---
## /user api
### 1. 회원가입
- **URL**: `/users/signup`
- **Method**: `POST`
- **Request Body**:
  - `username`: 사용자 이름 (string)
  - `password`: 비밀번호 (string)
  - `role`: 사용자 역할 (string)
- **Response**:
  - **200 OK**: 회원가입 성공 (사용자 정보 반환)
  - **400 Bad Request**: 요청 데이터가 유효하지 않을 경우.

### 2. 로그인
- **URL**: `/users/login`
- **Method**: `POST`
- **Request Body**:
  - `username`: 사용자 이름 (string)
  - `password`: 비밀번호 (string)
- **Response**:
  - **200 OK**: 로그인 성공 (JWT 토큰 반환)
  - **401 Unauthorized**: 인증 실패 시.

### 3. 내 정보 조회
- **URL**: `/users/me`
- **Method**: `GET`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Response**:
  - **200 OK**: 사용자 정보 반환
  - **401 Unauthorized**: 인증되지 않은 경우.

### 4. 내 정보 수정
- **URL**: `/users/me`
- **Method**: `PUT`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Request Body**:
  - `username`: 사용자 이름 (string)
  - `password`: 비밀번호 (string)
  - `role`: 사용자 역할 (string)
- **Response**:
  - **200 OK**: 사용자 정보 수정 성공
  - **400 Bad Request**: 요청 데이터가 유효하지 않을 경우.
  - **401 Unauthorized**: 인증되지 않은 경우.

### 5. 회원 탈퇴
- **URL**: `/users/me`
- **Method**: `DELETE`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Response**:
  - **200 OK**: 회원 탈퇴 성공
  - **401 Unauthorized**: 인증되지 않은 경우.
---
## /todo API 
### 1. TODO 생성
- **URL**: `/todos`
- **Method**: `POST`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Request Body**:
  - `title`: TODO 제목 (string)
  - `description`: TODO 설명 (string)
- **Response**:
  - **200 OK**: TODO 생성 성공 (생성된 TODO 반환)
  - **401 Unauthorized**: 인증되지 않은 경우.

### 2. TODO 목록 조회
- **URL**: `/todos`
- **Method**: `GET`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Response**:
  - **200 OK**: 로그인한 사용자의 모든 TODO 반환
  - **401 Unauthorized**: 인증되지 않은 경우.

### 3. 특정 TODO 조회
- **URL**: `/todos/{id}`
- **Method**: `GET`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Path Variable**:
  - `id`: 조회할 TODO의 ID (Long)
- **Response**:
  - **200 OK**: 특정 TODO 반환
  - **404 Not Found**: 해당 ID의 TODO이 존재하지 않는 경우.
  - **401 Unauthorized**: 인증되지 않은 경우.

### 4. TODO 수정
- **URL**: `/todos/{id}`
- **Method**: `PUT`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Path Variable**:
  - `id`: 수정할 TODO의 ID (Long)
- **Request Body**:
  - `title`: 수정할 TODO 제목 (string)
  - `description`: 수정할 TODO 설명 (string)
- **Response**:
  - **200 OK**: 수정된 TODO 반환
  - **404 Not Found**: 해당 ID의 TODO이 존재하지 않는 경우.
  - **401 Unauthorized**: 인증되지 않은 경우.

## 5. TODO 삭제
- **URL**: `/todos/{id}`
- **Method**: `DELETE`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Path Variable**:
  - `id`: 삭제할 TODO의 ID (Long)
- **Response**:
  - **204 No Content**: TODO 삭제 성공
  - **404 Not Found**: 해당 ID의 TODO이 존재하지 않는 경우.
  - **401 Unauthorized**: 인증되지 않은 경우.

### 6. 키워드로 TODO 검색
- **URL**: `/todos/search`
- **Method**: `GET`
- **Headers**:
  - `Authorization`: Bearer <JWT 토큰>
- **Query Parameter**:
  - `keyword`: 검색할 키워드 (string)
- **Response**:
  - **200 OK**: 키워드와 일치하는 TODO 목록 반환
  - **401 Unauthorized**: 인증되지 않은 경우.

--- 

###  **관리자 페이지**
- **URL**: `/admin`
- **Method**: `GET`
- **Headers**:
  - `Authorization: Bearer <JWT 토큰>`
- **Response**:
  - `200 OK`: 관리자 페이지 접근 성공
  - `403 Forbidden`: 권한 부족

---

### **테스트 실행**
```bash

./gradlew test --tests --info "com.example.todo.service.LoginTest"
./gradlew test --tests --info "com.example.todo.service.UserServiceTest" 
./gradlew test --tests --info "com.example.todo.service.TodoServiceTest" 

```
## 테스트 설명

### loginTest.java 설명
### 1. `signupTest`
    - 목적: 정상적인 회원가입 요청을 테스트.
    - 검증: 상태 코드가 200 OK인지 확인.

### 2. `signupWithExistingUsernameTest`
    - 목적: 이미 존재하는 사용자 이름으로 회원가입 시도 시 동작을 테스트.
    - 검증: 상태 코드가 400 Bad Request인지 확인.

### 3. `signupWithoutUsernameTest`
    - 목적: 사용자 이름 없이 회원가입 시도 시 동작을 테스트.
    - 검증: 상태 코드가 400 Bad Request인지 확인.
    
### 4. `signupWithoutPasswordTest`
    - 목적: 비밀번호 없이 회원가입 시도 시 동작을 테스트.
    - 검증:상태 코드가 400 Bad Request인지 확인.

### 5. `getUserNotJwtTest`
    - 목적: 사용자 jwt 토큰 없이 회원조회 시 동작을 테스트.
    - 검증: 상태 코드가 401 UNAUTHORIZED 인지 확인.
--- 
### UserServiceTest
### 1. `joinProcess`
- 목적: 새로운 사용자를 등록하는 로직을 테스트.
- 검증:
  - 사용자 정보가 올바르게 저장되었는지 확인.
  - 저장된 사용자 정보(`username`, `password`, `role`)가 기대값과 일치하는지 확인.
- 주요 동작:
  - `userRepository.save()` 메서드 호출 여부를 검증.

### 2. `updateUser`
- 목적: 기존 사용자의 정보를 업데이트하는 로직을 테스트.
- 검증:
  - 사용자 정보가 업데이트되었는지 확인.
  - `userRepository.saveAndFlush()` 메서드 호출 여부를 검증.

### 3. `deleteUser`
- 목적: 특정 사용자를 삭제하는 로직을 테스트.
- 검증:
  - 사용자가 삭제되었는지 확인.
  - `userRepository.delete()` 메서드 호출 여부를 검증.

### 4. `getUserDetail`
- 목적: 특정 사용자의 상세 정보를 조회하는 로직을 테스트.
- 검증:
  - 반환된 사용자 정보가 기대값과 일치하는지 확인.
  - `userRepository.findByUsername()` 메서드 호출 여부를 검증.

---

## TodoServiceTest

### 1. `createTodo`
- 목적: 새로운 TODO을 생성하고 저장하는 로직을 테스트.
- 검증:
  - 생성된 TODO이 데이터베이스에 저장되었는지 확인.
  - `todoRepository.save()` 메서드 호출 여부를 검증.

### 2. `getTodosByUser`
- 목적: 특정 사용자의 모든 TODO을 조회하는 로직을 테스트.
- 검증:
  - 반환된 TODO 목록이 기대값과 일치하는지 확인.
  - `todoRepository.findTodoByUserId()` 메서드 호출 여부를 검증.

### 3. `searchTodosByKeywordAndUser`
- 목적: 특정 키워드로 TODO을 검색하는 로직을 테스트.
- 검증:
  - 반환된 TODO 목록이 키워드와 일치하는지 확인.
  - `todoRepository.findByTitleContainingOrDescriptionContainingAndUser()` 메서드 호출 여부를 검증.

### 4. `updateTodo`
- 목적: 특정 TODO을 업데이트하는 로직을 테스트.
- 검증:
  - 업데이트된 TODO의 제목과 설명이 기대값과 일치하는지 확인.
  - `todoRepository.findByIdAndUserId()` 및 `todoRepository.save()` 메서드 호출 여부를 검증.

### 5. `deleteTodo`
- 목적: 특정 TODO을 삭제하는 로직을 테스트.
- 검증:
  - TODO이 삭제되었는지 확인.
  - `todoRepository.findByIdAndUserId()` 및 `todoRepository.delete()` 메서드 호출 여부를 검증.

---

## 공통 사항
- **Mocking**:
  - `@Mock`을 사용하여 `UserRepository`와 `TodoRepository`를 모킹.
  - `@InjectMocks`를 사용하여 `UserService`와 `TodoService`에 모킹된 객체를 주입.
- 검증:
  - `verify()`를 사용하여 특정 메서드가 호출되었는지 확인.
  - `assertNotNull`, `assertEquals` 등을 사용하여 반환값 검증.  

---

## 📂 디렉토리 구조
```
src
├── main
│   ├── java
│   │   └── com.example.todo
│   │       ├── config          # Spring Security 및 설정 파일
│   │       ├── controller      # REST 컨트롤러
│   │       ├── dto             # 데이터 전송 객체
│   │       ├── entity          # JPA 엔티티
│   │       ├── exception       # 예외 처리
│   │       ├── repository      # 데이터베이스 접근 레이어
│   │       ├── service         # 비즈니스 로직
│   │       └── util            # 유틸리티 클래스 (JWT 등)
│   └── resources
│       ├── application.properties # 환경 설정 파일
│       └── static              # 정적 리소스
└── test
    └── java
        └── com.example.todo.service    # 테스트 코드
            └── LoginTest.java          # 로그인 jwt 테스트 코드
            └── TodoServiceTest.java    # todo  추가, 조회, 수정, 삭제 기능 테스트 코드
            └── UserServiceTest.java    # user 추가, 조회, 수정, 삭제 기능 테스트 코드

```

---
