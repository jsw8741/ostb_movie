package com.example.ostb_movie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.constant.LikeStatus;
import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.dto.ReviewModifyDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Movie;
import com.example.ostb_movie.entity.Review;
import com.example.ostb_movie.entity.ReviewLike;
import com.example.ostb_movie.repository.MemberRepository;
import com.example.ostb_movie.repository.MovieRepository;
import com.example.ostb_movie.repository.ReviewLikeRepository;
import com.example.ostb_movie.repository.ReviewRepository;

import groovyjarjarantlr4.v4.parse.ANTLRParser.sync_return;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final MovieRepository movieRepository;
	private final MemberRepository memberRepository;
	private final ReviewLikeRepository reviewLikeRepository;
	
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
	public void updateReview(ReviewModifyDto reviewModifyDto) throws Exception {
		Review review = reviewRepository.findById(reviewModifyDto.getId())
								.orElseThrow(EntityNotFoundException::new);
		review.updateReview(reviewModifyDto);
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
	
	//본인확인
	public boolean validateReview(Long reviewId, String email) {
		Member curMember = memberRepository.findByEmail(email);
		Review review = reviewRepository.findById(reviewId)
							.orElseThrow(EntityNotFoundException::new);
		
		Member savedMember = review.getMember();
		
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		
		return true;
	}
	
	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(EntityNotFoundException::new);
		
		//delete
		reviewRepository.delete(review);
	}
	
	//리뷰에 좋아요 추가
	public void increaseLikeCount(Long reviewId, Long memberId) {
		
		ReviewLike reviewLike = reviewLikeRepository.likeToggle(memberId, reviewId);
		
		if(reviewLike != null) {
	        if(reviewLike.getLikeStatus().equals(LikeStatus.BEFORELIKE)) {
	        	reviewLike.setReviewLike(reviewLike.getReviewLike() + 1);
	        	reviewLike.setLikeStatus(LikeStatus.AFTERLIKE);
	        }else {
				reviewLike.setReviewLike(reviewLike.getReviewLike() - 1);
				reviewLike.setLikeStatus(LikeStatus.BEFORELIKE);
			}
		}
	}
	
	//좋아요 상태 확인
    public boolean toggleLike(Long memberId, Long reviewId) {
        // ReviewLike 엔티티를 통해 좋아요 상태 확인
        ReviewLike reviewLike = reviewLikeRepository.likeToggle(memberId, reviewId);
        
        if(reviewLike.getLikeStatus().equals(LikeStatus.BEFORELIKE)) {
        	return false;
        }else {
			return true;
		}
    }
	
}
