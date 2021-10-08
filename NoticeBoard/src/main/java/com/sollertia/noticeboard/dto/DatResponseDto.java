package com.sollertia.noticeboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatResponseDto {
    private Long id;
    private String content;
    private Long maxId;
    private String coordinate;
}
