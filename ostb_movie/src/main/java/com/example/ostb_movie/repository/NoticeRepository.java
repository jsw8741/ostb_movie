package com.example.ostb_movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.Notice;


public interface NoticeRepository extends JpaRepository<Notice, Long> {
	Page<Notice> findAllByOrderByIdDesc(Pageable pageable);
}
