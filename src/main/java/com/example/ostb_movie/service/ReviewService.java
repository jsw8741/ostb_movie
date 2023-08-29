package com.example.ostb_movie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Movie;
import com.example.ostb_movie.entity.Review;
import com.example.ostb_movie.repository.MovieRepository;
import com.example.ostb_movie.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final MovieRepository movieRepository;
	
	//리뷰 등록
	public Long saveReview(ReviewDto reviewDto) throws Exception {
		
		Review review = reviewDto.createReview();
		reviewRepository.save(review);
				
		return review.getId();
	}
	
	public Movie getReviewMovie(Long movieId) throws Exception {
		Movie movie = movieRepository.findById(movieId).orElseThrow(EntityNotFoundException::new);
		
		return movie;
	}
	
	public Long updateReview(ReviewDto reviewDto) throws Exception {
		Review review = reviewRepository.findById(reviewDto.getId())
								.orElseThrow(EntityNotFoundException::new);
		
		review.updateReview(reviewDto);
		
		return review.getId();
	}
	
	public List<ReviewDto> getReviewsByMemberId(Long memberId) {
	    List<Review> reviews = reviewRepository.findByReview(memberId);
	    return reviews.stream()
	            .map(ReviewDto::of)
	            .collect(Collectors.toList());
	}



}
