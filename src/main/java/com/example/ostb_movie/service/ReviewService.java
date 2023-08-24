package com.example.ostb_movie.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.entity.Review;
import com.example.ostb_movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	
	public Long saveReview(ReviewDto reviewDto) throws Exception {
		
		//리뷰 등록
		Review review = reviewDto.createReview();
		reviewRepository.save(review);
				
		return review.getId();
	}

}
