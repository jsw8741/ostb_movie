package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	
	@Query("select s.name from Seat s where s.theater.id = :theaterId")
	public List<String> getSeatList(@Param("theaterId") String theaterId);
}
