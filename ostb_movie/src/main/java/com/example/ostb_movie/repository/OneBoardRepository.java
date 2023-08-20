package com.example.ostb_movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.OneBoard;


public interface OneBoardRepository extends JpaRepository<OneBoard, Long> {
	Page<OneBoard> findAllByOrderByIdDesc(Pageable pageable);
}
