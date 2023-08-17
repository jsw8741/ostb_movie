package com.example.ostb_movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	@GetMapping(value = "/members/info")
	public String newMyPage() {
		return "member/myPage";
	}
	
	/* 작가 검색 팝업창 */
	@GetMapping(value = "/members/infoPop")
	public String pop() {
		return "member/myPagePop";
	}
}
