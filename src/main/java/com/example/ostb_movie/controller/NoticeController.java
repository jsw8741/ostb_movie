package com.example.ostb_movie.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.NoticeFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Notice;
import com.example.ostb_movie.repository.NoticeRepository;
import com.example.ostb_movie.service.NoticeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeController {
	
	private final NoticeService noticeService;
	private final NoticeRepository noticeRepository;
	
	//notice 생성페이지
		@GetMapping(value = "/notice/create")
		public String noticeForm(Model model) {
			model.addAttribute("noticeFormDto", new NoticeFormDto());
			return "notice/createNotice";
		}
		
	//notice 등록(insert)
	@PostMapping(value = "/notice/create")
	public String noticeNew(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult,
			Model model, Authentication authentication) {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
		if(bindingResult.hasErrors()) {
			return "notice/createNotice";
		}
		
		try {
		String email = member.getEmail();
		noticeService.saveNotice(noticeFormDto, email);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "공지사항 등록 중 에러가 발생했습니다.");
			return "notice/createNotice";
		}
		return "redirect:/notice/list";
	}
		
	//notice 리스트
	@GetMapping(value = {"/notice/list", "/notice/list/{page}" })
	public String noticeMainList(Model model,@PathVariable("page") Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Notice> notices = noticeService.getMainNoticeDtl(pageable);
		Long totalCount = noticeRepository.count();
		
		model.addAttribute("notices", notices);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "notice/listNotice";
	}
	
	
	//notice 수정페이지 보기
	@GetMapping(value = "/notice/update/{noticeId}")
	public String noticeDtl(@PathVariable("noticeId") Long noticeId, Model model) {
		
		try {
			Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
			model.addAttribute("notice", notice);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "공지사항 정보를 가져오는 중 에러가 발생했습니다.");
			model.addAttribute("notice", new Notice());
			return "/notice/list";
		}
		
		return "notice/updateNotice";
	}
	
	
	//notice 수정(update)
	@PostMapping(value = "/notice/update/{noticeId}" )
	public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, Model model, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "/notice/list";
		}
		
		
		try {
			noticeService.updateNotice(noticeFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "공지사항 수정 중 에러가 발생했습니다.");
			return "/notice/list";
		}
		return "redirect:/notice/list";
	}
	
	
	// notice 삭제(delete)
	@DeleteMapping("/notice/update/{noticeId}/delete")
	public @ResponseBody ResponseEntity deleteNotice(@PathVariable("noticeId") Long noticeId, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
		//1. 본인인증
		if(!noticeService.validateFaq(noticeId, member.getEmail())) {
			return new ResponseEntity<String>("공지사항 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		noticeService.deleteNotice(noticeId);
		
		return new ResponseEntity<Long>(noticeId, HttpStatus.OK);
		
	}
	
	//notice 상세보기
	@GetMapping(value = "/notice/read/{noticeId}")
	public String noticeDtllist(@PathVariable("noticeId") Long noticeId, Model model) {
		
		try {
			Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
			model.addAttribute("notice", notice);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "공지사항 상세정보를 가져오는 중 에러가 발생했습니다.");
			model.addAttribute("notice", new Notice());
			return "/notice/listNotice";
		}
		
		return "notice/readNotice";
	}
	
}
