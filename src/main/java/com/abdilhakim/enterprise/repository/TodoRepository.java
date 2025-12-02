package com.abdilhakim.enterprise.repository;

import com.abdilhakim.enterprise.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository <Todo, Long> {
    List<Todo> findByUser_Email(String email);
    Optional<Todo> findByIdAndUser_Email(Long id, String email);

}

