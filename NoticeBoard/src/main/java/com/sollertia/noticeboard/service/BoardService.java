package com.sollertia.noticeboard.service;

import com.sollertia.noticeboard.dto.BoardRequestDTO;
import com.sollertia.noticeboard.model.Board;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.repository.BoardRepository;
import com.sollertia.noticeboard.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional // board 의 모든 리스트 작성일 기분 DESC로 가져오기
    public Page<Board> list(Pageable pageable){
        //List<Board> list = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));  return list;
        return boardRepository.findAll(pageable);

    }

    @Transactional // 게시글 등록
    public String insert(BoardRequestDTO boardRequestDTO,User user){
          Board board = new Board(boardRequestDTO,user);
          boardRepository.save(board);
          return "등록성공";
    }

    @Transactional // 게시글, 댓글 상세보기
    public Board detail(Long id) throws Exception {

        Optional<Board> boardOptional = boardRepository.findById(id);
        if(boardOptional.isPresent()) {
            Board board = boardOptional.get();
            return board;
        } else {
            throw new Exception();
        }
    }

    @Transactional // 글 수정 하기
    public String update(BoardRequestDTO boardRequestDTO, User user){
        Board board = boardRepository.findById(boardRequestDTO.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 글은 없는 글입니다.")
        );
        if (board.getWriter().equals(user.getUsername())){
            board.update(boardRequestDTO);
            return "수정완료";
        }else{
            return "작성자가 아닙니다.";
        }
    }

    @Transactional // 글 삭제 하기
    public String delete(BoardRequestDTO boardRequestDTO, User user){
         try {
             Optional<Board> boardOptional = boardRepository.findById(boardRequestDTO.getId());
             if(boardOptional.isPresent()) {
                 Board board = boardOptional.get();
                 if (board.getWriter().equals(user.getUsername())){
                     boardRepository.deleteById(boardRequestDTO.getId());
                     return "삭제완료";
                 }else{
                     return "작성자가 아닙니다.";
                 }
             } else {
                 return "해당 글은 없습니다.";
             }
        } catch (Exception e) {
            e.printStackTrace();
            return "작성자가 아닙니다.";
        }
    }

}
