package com.sollertia.noticeboard.service;

import com.sollertia.noticeboard.dto.BoardRequestDTO;
import com.sollertia.noticeboard.dto.DatRequestDTO;
import com.sollertia.noticeboard.dto.DatResponseDto;
import com.sollertia.noticeboard.model.Board;
import com.sollertia.noticeboard.model.Dat;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.repository.DatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatService {

    private final DatRepository datRepository;

    // 상세글 댓글 DESC SORT 해서 가져오기
    @Transactional
    public List<Dat> datRead(Long lid) {

        return datRepository.findAllByBoardIdOrderByIdDesc(lid);
    }

    // 댓글 등록
    @Transactional
    public DatResponseDto datInsert(DatRequestDTO datRequestDTO, User user) {
        Dat dat = new Dat(datRequestDTO,user);
        datRepository.save(dat);
        // maxId를 저장하기위한 응답DTO
        Long maxId = datRepository.getMaxDatId();
        DatResponseDto responseDto = new DatResponseDto();
        responseDto.setContent(dat.getContent());
        responseDto.setId(dat.getId());
        responseDto.setMaxId(maxId);

        return responseDto;
    }

    // 댓글 수정
    @Transactional
    public void datUpdate(DatRequestDTO datRequestDTO) {
        Dat dat = datRepository.findById(datRequestDTO.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 글은 없는 댓글입니다.")
        );
        dat.update(datRequestDTO);
    }

    // 댓글 삭제
    @Transactional
    public void datDelete(DatRequestDTO datRequestDTO){
        Dat dat = datRepository.findById(datRequestDTO.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 글은 없는 글입니다.")
        );
        datRepository.deleteById(datRequestDTO.getId());
    }


}
