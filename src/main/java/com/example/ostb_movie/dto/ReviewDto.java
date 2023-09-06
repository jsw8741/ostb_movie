package com.example.ostb_movie.dto;

import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Movie;
import com.example.ostb_movie.entity.Review;
import com.querydsl.core.annotations.QueryProjection;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class ReviewDto {
	private Long id;
	
	@NotNull(message = "내용은 필수로 입력해야합니다")
	private String content;
	
	private LocalDateTime reviewDate;
	
	private Member member;
	
	private Movie movie;
	
	private int rvLike;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Review createReview() {
		Review review = modelMapper.map(this, Review.class);
		review.setReviewDate(LocalDateTime.now());
		return review;
	}
	
	public static ReviewDto of (Review review) {
		return modelMapper.map(review, ReviewDto.class);
	}
	
}
