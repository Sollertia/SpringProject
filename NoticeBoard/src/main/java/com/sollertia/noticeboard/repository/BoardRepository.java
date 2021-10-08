package com.sollertia.noticeboard.repository;

import com.sollertia.noticeboard.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<Board,Long> {


}
