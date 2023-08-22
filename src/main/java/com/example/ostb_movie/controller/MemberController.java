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
	//마이페이지 화면
	public String newMyPage(Authentication authentication, Model model) {
		
		// 로그인한 사용자의 정보를 담고있다.
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
	public String popProfile(@Valid MypageFormDto mypageFormDto, Authentication authentication,
			Model model, BindingResult bindingResult, 
			@RequestParam("memberImg") MultipartFile memberImgFile) {
		
		if(bindingResult.hasErrors()) {
			return "member/myPagePop";
		}
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        
        

		if(memberImgFile.isEmpty()) {
			model.addAttribute("errorMessage", "이미지가 존재하지 않습니다.");
			return "member/myPagePop";
		}
		
		try {
			memberService.updateMember(mypageFormDto, memberImgFile);
			
//			member.updateMember(mypageFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "프로필 등록 중 에러가 발생했습니다.");
			return "member/myPagePop";
		}
		
		model.addAttribute("member",member);
		model.addAttribute("mypageFormDto",mypageFormDto);
//		return "redirect:/member/myPage";
		return "member/myPage";
	}
}
