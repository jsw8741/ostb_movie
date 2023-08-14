package com.example.ostb_movie.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.MemberFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/login/loginForm")
	public String loginForm() {
		return "/login/loginForm";
	}
	
	// 회원가입 화면
	@GetMapping("/login/joinForm")
	public String joinForm(Authentication authentication, Model model) {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        
        System.out.println(member);
        
        if(member != null) {
        	MemberFormDto memberFormDto = MemberFormDto.of(member);
        	
        	model.addAttribute("memberFormDto", memberFormDto);
        	System.out.println(memberFormDto.getProvider()+"FFFFFFFF");
        }else {    
        	model.addAttribute("memberFormDto", new MemberFormDto());
        }
		
		return "/login/joinForm";
	}
	
	// 회원가입
	@PostMapping(value = "/login/join")
	public String joinNew(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
		if(bindingResult.hasErrors()) {
			// 에러가 있다면 회원가입 페이지로 이동
			return "/login/joinForm";
		}
		
		try {
			// MemberFormDto -> Member Entity, 비밀번호 암호화(일반 회원 가입일때만)
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "/login/joinForm";
		}
		
		return "/login/loginForm";
	}
	
	// 로그인 실패
	@GetMapping(value = "/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
		return "login/loginForm";
	}
}
