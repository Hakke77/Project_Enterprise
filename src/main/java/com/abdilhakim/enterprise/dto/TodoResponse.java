package com.abdilhakim.enterprise.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Instant createdAt;
}
