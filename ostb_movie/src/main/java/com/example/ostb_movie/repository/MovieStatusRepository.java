package com.example.ostb_movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.MovieStatus;

public interface MovieStatusRepository extends JpaRepository<MovieStatus, Long>{
	
	@Query("select m from MovieStatus m where m.status = :status")
	MovieStatus getStatus(@Param("status") String status);
}
