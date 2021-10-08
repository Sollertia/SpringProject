package com.sollertia.noticeboard.repository;

import com.sollertia.noticeboard.model.Dat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DatRepository extends JpaRepository<Dat, Long> {

        List<Dat> findAllByBoardIdOrderByIdDesc(Long boardId);
        // 가장 마지막글 번호 가져오기
        @Query(value = "SELECT max(id) FROM Dat")
        Long getMaxDatId();
}
