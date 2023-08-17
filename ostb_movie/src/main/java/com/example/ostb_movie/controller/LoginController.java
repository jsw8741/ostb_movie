package com.example.ostb_movie.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.MemberFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.service.EmailService;
import com.example.ostb_movie.service.EmailServiceImpl;
import com.example.ostb_movie.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	
	// 로그인 화면
	@GetMapping("/login/loginForm")
	public String loginForm() {
		return "/login/loginForm";
	}
	
	// 회원가입 화면
	@GetMapping("/login/joinForm")
	public String joinForm(Authentication authentication, Model model) {
		
		if(authentication != null) {
			PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
	        Member member = principal.getMember();
	        if(member != null) {
	        	MemberFormDto memberFormDto = MemberFormDto.of(member);
	        	model.addAttribute("memberFormDto", memberFormDto);
	        }
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
	
	// 이메일 찾기 화면
	@GetMapping(value = "/login/find/email")
	public String findEmailForm() {
		
		return "login/findEmailForm";
	}
	
	// 이메일 찾기
	@PostMapping(value = "/login/find/email")
	public String findEmail(@RequestParam("name") String name, @RequestParam("phone") String phone,
            Model model) {
		
		try {
			String memberEmail = memberService.findEmail(name, phone);
			model.addAttribute("name", name);
			model.addAttribute("email", memberEmail);
			
			return "login/findEmail";
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "/login/findEmailForm";
		}
		
	}
	
	// 비밀번호 찾기 화면
	@GetMapping(value = "/login/find/pw")
	public String findPwForm() {
		return "login/findPwForm";
	}
	
	// 비밀번호 찾기
	@PostMapping(value = "/login/find/pw")
	public String findPw(@RequestParam("email") String email, Model model) throws Exception{
		Member member = new Member();
		
		try {
			// 1. 입력한 이메일이 회원인지 확인
			member = memberService.findMember(email);
			
			// 2. 입력한 메일로 임시 비밀번호 전송
			String tempPassword = EmailServiceImpl.createTempPassword();
			emailService.sendSimpleMessage(email, tempPassword);
			
			// 소셜 로그인 / 회원은 불가능 
			if(member.getPassword().equals("SNS 로그인")) {
				model.addAttribute("errorMessage", "소셜 로그인 / 회원 가입 회원은 비밀번호 변경이 불가능합니다.");
				return "/login/findPwForm";
			}else {
			
			// 3. 기존 비밀번호를 임시 비밀번호로 변경
			member.setPassword(passwordEncoder.encode(tempPassword));
			member = memberService.updatePassword(member);
			model.addAttribute("tempPasswordMessage", "이메일 확인 후 임시 비밀번호로 로그인해주세요.");
			
			return "login/loginForm";
			}
			
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "/login/findEmailForm";
		}
		
	}
}
