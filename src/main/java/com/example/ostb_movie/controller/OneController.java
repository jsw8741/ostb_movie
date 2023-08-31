package com.example.ostb_movie.controller;

import java.util.List;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.OneBoardFormDto;
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
	public String chattForm(Model model, Authentication authentication) {
		
		model.addAttribute("oneBoardFormDto",new OneBoardFormDto());
		return "chatt/createChatt";
	}
	
	//1:1 채팅방 등록(insert)
	@PostMapping(value = "/chatt/createChatt")
	public String chattNew(@Valid OneBoardFormDto oneBoardFormDto, BindingResult bindingResult,
			Model model, Authentication authentication) {
		
	    if (!authentication.isAuthenticated()) {
	        return "redirect:/"; // 로그인이 되어 있지 않은 경우 로그인 페이지로 리다이렉트
	    }
		
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        
		if(bindingResult.hasErrors()) {
			return "redirect:/";
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
	
	
	//채팅방 리스트(관리자)
	@GetMapping(value = {"/chatt/list", "/chatt/list/{page}"})
	public String chattMainList(Model model, @PathVariable("page") Optional<Integer> page ) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<OneBoard> oneBoards = oneBoardRepository.findAllByOrderByRoomStatusDescRegTimeDesc(pageable);
		Long totalCount = oneBoardRepository.count();
		
		model.addAttribute("oneBoards", oneBoards);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "chatt/listChatt";
	}
	
	//채팅방 연결
	@PostMapping(value = "/chatt")
	public @ResponseBody ResponseEntity<String> chattClose(@RequestBody @Valid OneBoardFormDto oneBoardFormDto,
			BindingResult bindingResult, Authentication authentication, @PathVariable("roomId") String roomId) {
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			
			for(FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
			
		}
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
		
        try {
        } catch (Exception e) {
        	return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
		return new ResponseEntity<String>(roomId, HttpStatus.OK);
	}
	
	//채팅방 종료
	@PostMapping("/chatt/{roomId}/chattClose")
	public @ResponseBody ResponseEntity closeChatt(@PathVariable("roomId") String roomId, Authentication authentication) {
		roomId = chattService.closeChatt(roomId);
		System.out.println("sssssss" + roomId);
		return new ResponseEntity<String>(roomId, HttpStatus.OK);
	}
	
	
}