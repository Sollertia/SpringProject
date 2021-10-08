package com.sollertia.noticeboard.model;

import com.sollertia.noticeboard.dto.BoardRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter // test데이터 생성용
@Getter
@NoArgsConstructor
@Entity
public class Board extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    // 글 등록 시 이용
    public Board(BoardRequestDTO boardDTO, User user){
        this.title = boardDTO.getTitle();
        this.content = boardDTO.getContent();
        this.writer = user.getUsername();
    }
    // 글 수정 시 이용
    public void update(BoardRequestDTO boardRequestDTO){
        this.title = boardRequestDTO.getTitle();
        this.content = boardRequestDTO.getContent();
    }

    // 글 검색 시 이용



}
