package com.sollertia.noticeboard.model;

import com.sollertia.noticeboard.dto.BoardRequestDTO;
import com.sollertia.noticeboard.dto.DatRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

    @Setter // test데이터 생성용
    @Getter
    @NoArgsConstructor
    @Entity
    public class Dat extends Timestamped {

        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Long id;

        @Column(nullable = false)
        private String writer;

        @Column(nullable = false)
        private String content;

        @Column(nullable = false)
        private Long boardId;

        // 댓글 등록 시 이용
        public Dat(DatRequestDTO datRequestDTO, User user) {
            this.content = datRequestDTO.getContent();
            this.writer = user.getUsername();
            this.boardId = datRequestDTO.getBoardId();
        }

        // 댓글 수정 시 이용
        public void update(DatRequestDTO datRequestDTO) {
            this.content = datRequestDTO.getContent();
        }

    }