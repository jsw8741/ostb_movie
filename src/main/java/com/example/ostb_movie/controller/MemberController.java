package com.example.ostb_movie.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.MypageFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.service.MemberService;
import com.fasterxml.jackson.annotation.JacksonInject.Value;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping(value =  "/members/info")
	public String newMyPage(Authentication authentication, Model model) {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
		
		model.addAttribute("member",member);
		
		return "member/myPage";
		
	}
	
	/* 작가 검색 팝업창 */
	@GetMapping(value = "/members/infoPop")
	public String pop(Authentication authentication, Model model) {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        
        MypageFormDto mypageFormDto = MypageFormDto.of(member);
        
		model.addAttribute("member",member);
		model.addAttribute("mypageFormDto",mypageFormDto);
		
		
		
		return "member/myPagePop";
	}
	
	//마이페이지 수정
	@PostMapping(value = "/members/infoPop")
	public String popProfile(@Valid MypageFormDto mypageFormDto,
			Model model, BindingResult bindingResult, @RequestParam("memberImg") MultipartFile memberImgFile) {
		//수정 정보들 가지고있어
		System.out.println("11111111111111111111111");
		if(bindingResult.hasErrors()) {
			System.out.println("666666666666666");
			return "member/myPagePop";
		}
		
		if(memberImgFile.isEmpty() && mypageFormDto.getId() == null) {
			model.addAttribute("errorMessage", "이미지가 존재하지 않습니다.");
			return "member/myPagePop";
		}
		
		try {
			memberService.updateMember(mypageFormDto, memberImgFile);
//			member.updateMember(mypageFormDto);
		} catch (Exception e) {
			System.out.println("33333333333333");
			e.printStackTrace();
			model.addAttribute("errorMessage", "프로필 등록 중 에러가 발생했습니다.");
			return "member/myPagePop";
		}
		
		return "member/myPagePop";
	}
}
