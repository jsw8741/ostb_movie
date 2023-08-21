package com.example.ostb_movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.Lost;

public interface LostRepository extends JpaRepository<Lost, Long>  {
	Page<Lost> findAllByOrderByIdDesc(Pageable pageable);
}
