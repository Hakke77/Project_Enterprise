package com.abdilhakim.enterprise.service;

import com.abdilhakim.enterprise.dto.TodoRequest;
import com.abdilhakim.enterprise.dto.TodoResponse;
import com.abdilhakim.enterprise.entity.Todo;
import com.abdilhakim.enterprise.entity.User;
import com.abdilhakim.enterprise.repository.TodoRepository;
import com.abdilhakim.enterprise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    private TodoResponse toResponse(Todo t) {
        return TodoResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .completed(t.isCompleted())
                .createdAt(t.getCreatedAt())
                .build();
    }

    @Transactional
    public TodoResponse createTodo(String userEmail, TodoRequest req) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Todo todo = Todo.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .completed(false)
                .user(user)
                .build();

        Todo saved = todoRepository.save(todo);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getTodos(String userEmail) {
        return todoRepository.findByUser_Email(userEmail).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TodoResponse getTodo(String userEmail, Long id) {
        Todo t = todoRepository.findByIdAndUser_Email(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        return toResponse(t);
    }

    @Transactional
    public TodoResponse updateTodo(String userEmail, Long id, TodoRequest req) {
        Todo t = todoRepository.findByIdAndUser_Email(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        if (req.getTitle() != null) t.setTitle(req.getTitle());
        if (req.getDescription() != null) t.setDescription(req.getDescription());
        // keep completed update separate in controller or allow here (we'll provide endpoint)
        Todo saved = todoRepository.save(t);
        return toResponse(saved);
    }

    @Transactional
    public void setCompleted(String userEmail, Long id, boolean completed) {
        Todo t = todoRepository.findByIdAndUser_Email(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        t.setCompleted(completed);
        todoRepository.save(t);
    }

    @Transactional
    public void deleteTodo(String userEmail, Long id) {
        Todo t = todoRepository.findByIdAndUser_Email(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todoRepository.delete(t);
    }
}
