package com.abdilhakim.enterprise.controller;

import com.abdilhakim.enterprise.dto.TodoRequest;
import com.abdilhakim.enterprise.dto.TodoResponse;
import com.abdilhakim.enterprise.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/me")
    public String me() {
        return "Du är inloggad och detta är en skyddad endpoint!";
    }

    // List   user's todos
    @GetMapping
    public ResponseEntity<List<TodoResponse>> list(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(todoService.getTodos(email));
    }

    // Create
    @PostMapping
    public ResponseEntity<TodoResponse> create(Principal principal, @RequestBody TodoRequest req) {
        String email = principal.getName();
        return ResponseEntity.ok(todoService.createTodo(email, req));
    }

    // Get one
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getOne(Principal principal, @PathVariable Long id) {
        String email = principal.getName();
        return ResponseEntity.ok(todoService.getTodo(email, id));
    }

    // Update (title/description)
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> update(Principal principal, @PathVariable Long id, @RequestBody TodoRequest req) {
        String email = principal.getName();
        return ResponseEntity.ok(todoService.updateTodo(email, id, req));
    }

    // Mark completed / uncompleted
    @PatchMapping("/{id}/completed")
    public ResponseEntity<Void> setCompleted(Principal principal, @PathVariable Long id, @RequestParam boolean value) {
        String email = principal.getName();
        todoService.setCompleted(email, id, value);
        return ResponseEntity.ok().build();
    }


    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable Long id) {
        String email = principal.getName();
        todoService.deleteTodo(email, id);
        return ResponseEntity.noContent().build();
    }
}

