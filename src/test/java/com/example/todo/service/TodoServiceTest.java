package com.example.todo.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todo.dto.TodoDTO;
import com.example.todo.entity.TodoEntity;
import com.example.todo.entity.UserEntity;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    TodoService todoService;

    @InjectMocks
    UserService userService;

    @Mock
    TodoRepository todoRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void createTodo() {
        log.info("테스트 start : createTodo");

        // 테스트: 새로운 Todo를 생성하고 저장하는 기능을 검증
        // given
        TodoDTO requestDTO = new TodoDTO();
        requestDTO.setTitle("제목1");
        requestDTO.setDescription("내용1");

        UserEntity user = new UserEntity();
        user.setRole("ROLE_ADMIN");
        user.setUsername("test");
        user.setId(1L);

        TodoEntity saved = new TodoEntity();
        ReflectionTestUtils.setField(saved, "id", 1L);
        saved.setTitle("제목");
        saved.setDescription("내용");
        given(todoRepository.save(any(TodoEntity.class))).willReturn(saved);

        // when
        TodoEntity todo = todoService.createTodo(requestDTO, user);

        // then
        assertTrue(todo.getId() != null); // Todo가 저장되었는지 확인
        verify(todoRepository).save(any(TodoEntity.class)); // save 메서드 호출 확인

        log.info("테스트 end: createTodo");
    }

    @Test
    void getTodosByUser() {
        // 테스트: 특정 사용자의 모든 Todo를 조회하는 기능을 검증
        // given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");
        user.setRole("ROLE_ADMIN");

        TodoEntity todo1 = new TodoEntity();
        todo1.setId(1L);
        todo1.setTitle("할 일 1");
        todo1.setDescription("설명 1");
        todo1.setUser(user);

        TodoEntity todo2 = new TodoEntity();
        todo2.setId(2L);
        todo2.setTitle("할 일 2");
        todo2.setDescription("설명 2");
        todo2.setUser(user);

        List<TodoEntity> todos = List.of(todo1, todo2);

        // Mocking the repository call
        given(todoRepository.findTodoByUserId(user.getId())).willReturn(todos);

        // when
        List<TodoEntity> result = todoService.getTodosByUser(user);

        log.info("Result: " + result.toString());

        // then
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(2, result.size()); // 반환된 할 일 개수 확인
        assertEquals("할 일 1", result.get(0).getTitle()); // 첫 번째 할 일 제목 확인
        assertEquals("할 일 2", result.get(1).getTitle()); // 두 번째 할 일 제목 확인
        verify(todoRepository).findTodoByUserId(user.getId()); // findByUserId 메서드가 호출되었는지 확인

        log.info("테스트 end : getTodosByUser");

    }

    @Test
    void updateTodo() {
        // 테스트: 특정 Todo를 업데이트하는 기능을 검증
        log.info("테스트 start : updateTodo");

        // given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        TodoEntity existingTodo = new TodoEntity();
        existingTodo.setId(1L);
        existingTodo.setTitle("기존 제목");
        existingTodo.setDescription("기존 설명");
        existingTodo.setUser(user);

        TodoDTO updatedTodoDTO = new TodoDTO();
        updatedTodoDTO.setTitle("새 제목");
        updatedTodoDTO.setDescription("새 설명");

        given(todoRepository.findByIdAndUserId(1L, user.getId())).willReturn(Optional.of(existingTodo));
        given(todoRepository.save(any(TodoEntity.class))).willReturn(existingTodo);

        // when
        TodoEntity result = todoService.updateTodo(1L, updatedTodoDTO, user);

        // then
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals("새 제목", result.getTitle()); // 제목이 업데이트되었는지 확인
        assertEquals("새 설명", result.getDescription()); // 설명이 업데이트되었는지 확인
        verify(todoRepository).findByIdAndUserId(1L, user.getId()); // findByIdAndUserId 호출 확인
        verify(todoRepository).save(existingTodo); // save 호출 확인

        log.info("테스트 end : updateTodo");

    }

    @Test
    void deleteTodo() {
        // 테스트: 특정 Todo를 삭제하는 기능을 검증
        log.info("테스트 start : deleteTodo");

        // given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        TodoEntity todo = new TodoEntity();
        todo.setId(1L);
        todo.setUser(user);

        given(todoRepository.findByIdAndUserId(1L, user.getId())).willReturn(Optional.of(todo));

        // when
        todoService.deleteTodo(1L, user);

        // then
        verify(todoRepository).findByIdAndUserId(1L, user.getId()); // findByIdAndUserId 호출 확인
        verify(todoRepository).delete(todo); // delete 호출 확인
        
        log.info("테스트 end : deleteTodo");

    }

    @Test
    void searchTodosByKeywordAndUser() {
        // 테스트: 특정 키워드로 Todo를 검색하는 기능을 검증
        log.info("테스트 start : searchTodosByKeywordAndUser");
        
        // given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        TodoEntity todo1 = new TodoEntity();
        todo1.setId(1L);
        todo1.setTitle("할 일 1");
        todo1.setDescription("설명 1");
        todo1.setUser(user);

        TodoEntity todo2 = new TodoEntity();
        todo2.setId(2L);
        todo2.setTitle("할 일 2");
        todo2.setDescription("설명 2");
        todo2.setUser(user);

        List<TodoEntity> todos = List.of(todo1, todo2);

        String keyword = "할 일";
        given(todoRepository.findByTitleContainingOrDescriptionContainingAndUser(keyword, keyword, user)).willReturn(todos);

        // when
        List<TodoEntity> result = todoService.searchTodosByKeywordAndUser("할 일", user);

        // then
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(2, result.size()); // 반환된 할 일 개수 확인
        assertEquals("할 일 1", result.get(0).getTitle()); // 첫 번째 할 일 제목 확인
        assertEquals("할 일 2", result.get(1).getTitle()); // 두 번째 할 일 제목 확인
        verify(todoRepository).findByTitleContainingOrDescriptionContainingAndUser(keyword, keyword, user); // findByTitleContainingAndUserId 호출 확인

        log.info("테스트 end : searchTodosByKeywordAndUser");
    }
}