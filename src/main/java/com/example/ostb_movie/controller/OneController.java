package com.example.ostb_movie.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.OneBoardFormDto;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.OneBoard;
import com.example.ostb_movie.repository.OneBoardRepository;
import com.example.ostb_movie.service.ChattService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OneController {
	private final ChattService chattService;
	private final OneBoardRepository oneBoardRepository;
	
	//1:1 채팅방 생성페이지
	@GetMapping(value = "/chatt/createChatt")
	public String chattForm(Model model) {
		model.addAttribute("oneBoardFormDto",new OneBoardFormDto());
		return "chatt/createChatt";
	}
	
	//1:1 채팅방 등록(insert)
	@PostMapping(value = "/chatt/createChatt")
	public String faqNew(@Valid OneBoardFormDto oneBoardFormDto, BindingResult bindingResult,
			Model model, Authentication authentication) {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
		if(bindingResult.hasErrors()) {
			return "/chatt/createChatt";
		}
		
		try {
		String email = member.getEmail();
		chattService.saveOneBoard(oneBoardFormDto, email);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "1:1문의 중 에러가 발생했습니다.");
			return "/chatt/createChatt";
		}
		return "redirect:/chatt/mychatt";
	}
	
	
	//나의 채팅방 보여주기
	@GetMapping(value = "/chatt/mychatt" )
	public String onechattlist(Model model, Authentication authentication) {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        long memberId = member.getId();
        List<OneBoard> oneBoards = chattService.getOneBoard(memberId);
        
        model.addAttribute("oneBoards", oneBoards);
		
		return "chatt/mychatt";
	}
	
	
	
	
	
	
	
	
	
}
