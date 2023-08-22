package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ostb_movie.entity.OneBoard;


public interface OneBoardRepository extends JpaRepository<OneBoard, Long> {
	Page<OneBoard> findAllByOrderByIdDesc(Pageable pageable);
	
	@Query(value = "SELECT * FROM one_board WHERE member_id = :memberId ORDER BY room_id DESC LIMIT 6", nativeQuery = true)
	List<OneBoard> getmyChatt(long memberId);
	
	
}
