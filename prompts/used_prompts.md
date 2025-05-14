# 사용 프롬프트 요약 정리

## 1. 인증 및 보안 관련
- JWT 기반 인증 및 설정
- Spring Security 설정 구성
- JWT 없이 요청 시 401 오류 처리
- AccessDeniedHandler, AuthenticationEntryPoint 설정
- 사용자 미존재시 UsernameNotFoundException 처리
- JWT 토큰 인증 흐름 구성

## 2. 예외 처리 및 상태 코드
- GlobalExceptionHandler 구현
- BusinessException 등 커스텀 예외 정의
- 인증/인가 오류 시 401, 403, 404 응답 처리 방식
- InternalAuthenticationServiceException → 404로 전환

## 3. JPA 및 데이터베이스
- Member, Todo 엔티티 관계 설정
- LazyInitializationException 해결
- Member로 Todo 검색 시 무한 참조 해결
- TodoService에서 로그인 사용자 기준 처리

## 4. 테스트 및 커버리지
- 단위 테스트/통합 테스트 구성
- Mockito, MockMvc 설정
- 테스트용 Member, TodoDto 접근 제한 이슈 해결
- `@Test`, `@SpringBootTest` 사용

## 5. Gradle 및 빌드 설정
- Spring Boot + Java 17 빌드 설정
- lombok, jjwt, mockito 등 의존성 추가
- `jacocoTestReport`로 커버리지 측정
- `testImplementation` 및 `testRuntimeOnly` 구성

## 6. 컨트롤러/서비스/DTO
- TodoController에서 @AuthenticationPrincipal 처리
- 로그인 사용자 기준 할 일 조회 및 검색 구현
- `Member`, `TodoDto` 생성자 접근 제어 해결
- 서비스 계층 분리 및 DTO 활용

## 7. 에러 처리 및 디버깅
- AssertionFailedError 대응
- 메서드 명명 규칙 (`unvalidIdTest`) 문제 해결
- `retrieveUser()` 오버라이드 불가 이슈 분석
- 403 대신 401 응답 설정 문제 디버깅
- JSON 응답 생성 오류 (`write` 메서드 오용) 해결

## 8. 프로젝트 관리
- 사용한 프롬프트를 md 파일로 정리 요청
- 전체 프롬프트를 카테고리별 요약 정리 요청
