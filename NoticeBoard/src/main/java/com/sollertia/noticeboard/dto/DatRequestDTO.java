package com.sollertia.noticeboard.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DatRequestDTO {
    private Long id;
    private String content;
    private Long boardId;
    private String coordinate;
}
