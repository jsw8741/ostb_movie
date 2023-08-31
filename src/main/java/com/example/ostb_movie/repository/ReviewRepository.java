package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	// 회원이 쓴 리뷰 리스트
	@Query("select r from Review r where r.member.id = :member_id")
	Page<Review> getMyReviewPage(@Param("member_id") Long memberId, Pageable pageable);
	
	// 해당 영화의 리뷰 리스트
	@Query("select r from Review r where r.movie.id = :movie_id")
	List<Review> getMovieReviewAll(@Param("movie_id") Long movie_id);
}
