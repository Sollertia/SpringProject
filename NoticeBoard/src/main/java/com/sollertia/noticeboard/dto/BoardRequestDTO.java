package com.sollertia.noticeboard.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardRequestDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
}
