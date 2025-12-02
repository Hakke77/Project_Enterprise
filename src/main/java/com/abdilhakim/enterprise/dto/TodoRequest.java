package com.abdilhakim.enterprise.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequest {
    private String title;
    private String description;
}
