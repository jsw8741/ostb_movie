package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.OneBoard;


public interface OneBoardRepository extends JpaRepository<OneBoard, Long> {
	Page<OneBoard> findAllByOrderByRoomStatusDescRegTimeDesc(Pageable pageable);
	
	@Query(value = "select * from one_board where member_id = :memberId order by room_id desc limit 6", nativeQuery = true)
	List<OneBoard> getmyChatt(long memberId);
	
	@Query(value = "select created_by from one_board where room_id = :roomId", nativeQuery = true)
	String getCreatedBy(@Param("roomId") String roomId);
	
	@Query(value = "select * from one_board where room_id = :roomId", nativeQuery = true)
	OneBoard getId(@Param("roomId") String roomId);
	
	
	
	
}
