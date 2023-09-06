package com.example.ostb_movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
	//좋아요 토글
	@Query("select rl from ReviewLike rl where rl.member.id = :member_id and rl.review.id = :review_id")
	ReviewLike likeToggle(@Param("member_id") Long member_id, @Param("review_id") Long review_id);
}
