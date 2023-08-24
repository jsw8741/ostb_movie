package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	@Query("select r from Review r where r.member.id = :member_id")
	List<Review> findByReview(@Param("member_id") Long memberId);
}
