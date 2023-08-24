package com.example.ostb_movie.dto;

import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Review;

import lombok.*;

@Getter
@Setter
public class ReviewDto {
	private Long id;
	
	private String content;
	
	private int rvLike;
	
	private LocalDateTime reviewDate;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Review createReview() {
		return modelMapper.map(this, Review.class);
	}
	
	public static ReviewDto of (Review review) {
		return modelMapper.map(review, ReviewDto.class);
	}
	
}
