package com.example.ostb_movie.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
	List<Theater> findByStartTimeBetween(LocalDateTime start, LocalDateTime end, Sort sort);
}