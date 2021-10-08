package com.sollertia.noticeboard.controller;

import com.sollertia.noticeboard.dto.DatRequestDTO;
import com.sollertia.noticeboard.dto.DatResponseDto;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.security.UserDetailsImpl;
import com.sollertia.noticeboard.service.DatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DatController {

    private final DatService datService;

    // 댓글 작성 - Ajax
    @PostMapping("/dat/insert")
    public DatResponseDto datInsert(@RequestBody DatRequestDTO datRequestDTO,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
            User user = userDetails.getUser();

        return datService.datInsert(datRequestDTO,user);
    }

    //댓글 수정 - Ajax
    @PostMapping("/dat/update")
    public void datUpdate(@RequestBody DatRequestDTO datRequestDTO){
            datService.datUpdate(datRequestDTO);
    }


    // 댓글 삭제 - Ajax
    @PostMapping("/dat/delete")
    public DatResponseDto datDelete(@RequestBody DatRequestDTO datRequestDTO){

        DatResponseDto datResponseDto = new DatResponseDto();
        datResponseDto.setCoordinate(datRequestDTO.getCoordinate());

        datService.datDelete(datRequestDTO);

        return datResponseDto;
    }




}
