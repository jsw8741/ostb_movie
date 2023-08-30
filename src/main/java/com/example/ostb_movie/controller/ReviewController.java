package com.example.ostb_movie.controller;

import java.util.List;

import java.security.Principal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.dto.ReviewModifyDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Movie;
import com.example.ostb_movie.entity.Review;
import com.example.ostb_movie.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	//리뷰 등록
	@PostMapping(value = "/member/myPage")
	public String reviewNew(@Valid ReviewDto reviewDto, BindingResult bindingResult, @RequestParam("movieId") Long movieId,
			Model model, Authentication authentication) {
		if(bindingResult.hasErrors()) {
			return "member/myPage";
		}
		
		try {
			
			//1. 현재 로그인한 회원을 찾아서 Dto Member에 넣는다.
			PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
	        Member member = principal.getMember();
			
			reviewDto.setMember(member);
			
			//2. 현재 들어와있는 상세페이지에서 id를 받아와 movie을 찾아서 Dto Member에 넣는다.
			Movie reviewMovie = reviewService.getReviewMovie(movieId);
			
			reviewDto.setMovie(reviewMovie);
			
			reviewService.saveReview(reviewDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "리뷰 등록 중 에러가 발생했습니다.");
			return "member/myPage";
		}
		
		return "redirect:/";
	}

	//내가 쓴 리뷰 페이지보기
	@GetMapping(value =  { "/member/reviewPage/{memberId}", "/member/reviewPage/{memberId}/{page}" })
	public String reviewDtl(Model model, @PathVariable("memberId") Long memberId, @PathVariable("page") Optional<Integer> Page) {
		Pageable pageable = PageRequest.of(Page.isPresent() ? Page.get() : 0, 6);
		
		Page<Review> reviews = reviewService.getMyReviewPage(memberId, pageable);
		
		model.addAttribute("memberId", memberId);
		model.addAttribute("reviews", reviews);
		model.addAttribute("maxPage", 5); //리뷰 페이지 하단에 보여줄 최대 페이지 번호
		
		return "member/reviewPage";
	}
	
	//수정화면
	@GetMapping(value = "/member/reviewUpdatePage/{reviewId}")
	public String showReviewUpdateForm(@PathVariable Long reviewId, Model model) {
		ReviewDto reviewDto = reviewService.getModifyReview(reviewId);
		
		model.addAttribute("reviewModifyDto", reviewDto);
		
		return "member/reviewUpdatePage";
	}
	
	//리뷰수정
	@PostMapping(value = "/member/reviewUpdatePage/{reviewId}")
	public String reviewUpdate(@Valid ReviewModifyDto reviewModifyDto, Model model, 
			BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "redirect:/";
		}
		
		try {
			reviewService.updateReview(reviewModifyDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "리뷰 수정 중 에러가 발생했습니다.");
			model.addAttribute("reviewModifyDto", reviewModifyDto);
			return "redirect:/";
		}
		return "redirect:/";
	}
	
	//리뷰 삭제
	@DeleteMapping("/member/{reviewId}/delete")
	public @ResponseBody ResponseEntity deleteReview(@PathVariable("reviewId") Long reviewId, Authentication authentication, Model model) {
		
		//본인인증
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();

        if (!reviewService.validateReview(reviewId, member.getEmail())) {
            return new ResponseEntity<String>("리뷰 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        
        reviewService.deleteReview(reviewId);
        
        return new ResponseEntity<Long>(reviewId, HttpStatus.OK);
        
	}
}
