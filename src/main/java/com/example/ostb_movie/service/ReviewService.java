package com.example.ostb_movie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.dto.ReviewModifyDto;
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
	
	//리뷰 업데이트
	public Long updateReview(ReviewModifyDto reviewModifyDto) throws Exception {
		System.out.println("aaaaaaaaaaaaaaa");
		Review review = reviewRepository.findById(reviewModifyDto.getId())
								.orElseThrow(EntityNotFoundException::new);
		System.out.println("bbbbbbbbbbbbb");
		review.updateReview(reviewModifyDto);
		System.out.println("cccccccccccccc");
		return review.getId();
	}
	
	@Transactional(readOnly = true)
	public ReviewDto getReviewDtl(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
										.orElseThrow(EntityNotFoundException::new);
		
		ReviewDto reviewDto = ReviewDto.of(review);
		
		return reviewDto;
	}

	// 해당 영화의 모든 리뷰 가져오기
	public List<Review> getMovieReviewAll(Long movieId){
		List<Review> review = reviewRepository.getMovieReviewAll(movieId);
		
		return review;
	}
	
	// 내가 쓴 리뷰 페이징
	public Page<Review> getMyReviewPage(Long memberId, Pageable pageable){
		Page<Review> reviewPage = reviewRepository.getMyReviewPage(memberId, pageable);
		
		return reviewPage;
		
		
	}
	
	//수정할 리뷰 가져오기
	public ReviewDto getModifyReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(EntityNotFoundException::new);
		
		ReviewDto reviewDto = ReviewDto.of(review);
		
		return reviewDto;
	}
	
}
