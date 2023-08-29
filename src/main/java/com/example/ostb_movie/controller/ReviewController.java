package com.example.ostb_movie.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Movie;
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
		System.out.println(movieId + "KKKKKK");
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
	
	//리뷰 수정
	@PostMapping(value = "member/mypage1")
	public String updateReview(@Valid ReviewDto reviewDto, Model model,
			BindingResult bindingResult) {
	
	if(bindingResult.hasErrors()) {
		return "member/myPage";
	}
	
	try {
		reviewService.updateReview(reviewDto);
	} catch (Exception e) {
		e.printStackTrace();
		model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
		return "member/myPage";
	}
	return "redirect:/";
	}
	
	@GetMapping("/member/reviews")
	public String getMemberReviews(Model model, Authentication authentication) {
	    PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
	    Long memberId = principal.getMember().getId();

	    List<ReviewDto> memberReviews = reviewService.getReviewsByMemberId(memberId);
	    model.addAttribute("memberReviews", memberReviews);

	    return "member/reviews";
	}

}
