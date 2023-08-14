package com.example.ostb_movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.Faq;


public interface FaqRepository extends JpaRepository<Faq, Long> {
	Page<Faq> findAllByOrderByIdDesc(Pageable pageable);
}
