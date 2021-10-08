package com.sollertia.noticeboard.controller;
import com.sollertia.noticeboard.dto.BoardRequestDTO;
import com.sollertia.noticeboard.model.Board;
import com.sollertia.noticeboard.model.Dat;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.security.UserDetailsImpl;
import com.sollertia.noticeboard.service.BoardService;
import com.sollertia.noticeboard.service.DatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private final DatService datService;

    // 로딩시 모든 게시글 보여주기
    @GetMapping("/")
    public ModelAndView boardList(ModelAndView model,
                                  @RequestParam(required = false,defaultValue = "any") String chk,
                                  @PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        // 로그인한 유저가 로그인,회원가입페이지 접근시 메세지 출력
        model.addObject("chk", chk);

        Page<Board> page = boardService.list(pageable);
        List<Board> list = page.getContent();

        model.setViewName("boardList");
        model.addObject("page",page); // 페이지처리 값들
        model.addObject("list",list); // Pageable을 사용해서 Page 클래스에 게시글들이 쌓여버려서 끄집어내야 했음.
        return model;
    }

    // 글 작성 폼 보여주기
    @GetMapping("/board/write")
    public ModelAndView boardWrite(ModelAndView model){
        model.setViewName("boardWrite");
        return model;
    }

    // Ajax로 데이터 받아서 글 등록
    @PostMapping("/board/writen")
    public String boardWriten(@RequestBody BoardRequestDTO boardRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        String msg = boardService.insert(boardRequestDTO,user);
        return msg;
    }

    // 글 상세 및 해당글의 댓글 보여주기
    @GetMapping("/BoardDetail")
    public ModelAndView BoardDetail(ModelAndView model, @RequestParam() String id,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {

        Long Lid = Long.valueOf(id.trim()); // Long타입으로 변환
        // 상세글 내용 가져오기
        Board data = boardService.detail(Lid);
        // 상세글의 댓글 가져오기
        List<Dat> dat = datService.datRead(Lid);


        model.setViewName("boardDetail");
        model.addObject("data", data);
        model.addObject("dat",dat);
        try{
            model.addObject("username",userDetails.getUsername());
            return model;
        }catch (NullPointerException e){
            return model;
        }
    }

    // 글 수정 폼 보여주기
    @GetMapping("/board/update")
    public ModelAndView boardUpdate(ModelAndView model, @RequestParam() String id) throws Exception {

        Long Lid = Long.valueOf(id.trim()); // Long타입으로 변환
        Board data = boardService.detail(Lid);
        model.setViewName("boardUpdate");
        model.addObject("data", data);
        return model;
    }

    // Ajax로 데이터 받아서 글 수정
    @PostMapping("/board/updateComplete")
    public String updateComplete(@RequestBody BoardRequestDTO boardRequestDTO,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        String msg = boardService.update(boardRequestDTO,user);
        return msg;
    }

    // 글 삭제
    @PostMapping("/board/delete")
    public String boardDelete(@RequestBody BoardRequestDTO boardRequestDTO,
                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
       String msg = boardService.delete(boardRequestDTO,user);

       return msg;
    }
}
