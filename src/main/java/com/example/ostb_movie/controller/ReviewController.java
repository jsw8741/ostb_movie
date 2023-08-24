package com.example.ostb_movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
	
	//리뷰 등록
	@PostMapping(value = "/movie/movieDtl")
	public String reviewNew(@Valid ReviewDto reviewDto, BindingResult bindingResult,
			Model model) {
		if(bindingResult.hasErrors()) {
			return "/";
		}
		
		try {
			reviewService.saveReview(reviewDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "리뷰 등록 중 에러가 발생했습니다.");
			return "/";
		}
		
		return "member/myPage";
	}
	
}
