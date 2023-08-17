package com.example.ostb_movie.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ostb_movie.dto.FaqFormDto;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.repository.FaqRepository;
import com.example.ostb_movie.service.FaqService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FaqController {
	private final FaqService faqService;
	private final FaqRepository faqRepository;
	

	//faq 생성페이지
	@GetMapping(value = "/FAQ/createFAQ")
	public String faqForm(Model model) {
		model.addAttribute("faqFormDto", new FaqFormDto());
		return "FAQ/createFAQ";
	}
	
	//faq 등록(insert)
	@PostMapping(value = "/FAQ/createFAQ")
	public String faqNew(@Valid FaqFormDto faqFormDto, BindingResult bindingResult,
			Model model, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			return "/FAQ/createFAQ";
		}
		
		try {
		String email = principal.getName();
		faqService.saveFaq(faqFormDto, email);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ등록 중 에러가 발생했습니다.");
			return "/FAQ/createFAQ";
		}
		return "redirect:/FAQ/createFAQ";
	}
	
	// faq 리스트
	@GetMapping(value = { "/FAQ/list", "/FAQ/list/{page}" } )
	public String faqMainList(Model model,@PathVariable("page") Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Faq> faqs = faqService.getMainFaqDtl(pageable);
		Long totalCount = faqRepository.count();
		
		model.addAttribute("faqs", faqs);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "FAQ/listFAQ";
	}
	
	//faq 수정페이지 보기
	@GetMapping(value = "/members/centers/{faqId}")
	public String faqDtl(@PathVariable("faqId") Long faqId, Model model) {
		
		try {
			Faq faq = faqRepository.findById(faqId).orElseThrow(EntityNotFoundException::new);
			model.addAttribute("faq", faq);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ정보를 가져올때 에러가 발생했습니다.");
			model.addAttribute("faq", new Faq());
			return "member/createFaq";
		}
		return "member/faqUpdate";
	}
	
	//faq 수정(update)
	@PostMapping(value = "/members/centers/{faqId}" )
	public String faqUpdate(@Valid FaqFormDto faqFormDto, Model model, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "member/faqUpdate";
		}
		
		try {
			faqService.updateFaq(faqFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ 수정 중 에러가 발생했습니다.");
			return "member/faqUpdate";
		}
		return "redirect:/members/center";
	}
	
	// faq 삭제(delete)
	@DeleteMapping("/members/centers/{faqId}/delete")
	public @ResponseBody ResponseEntity deleteFaq(@PathVariable("faqId") Long faqId, Principal principal) {
		
		//1. 본인인증
		if(!faqService.validateFaq(faqId, principal.getName())) {
			return new ResponseEntity<String>("FAQ 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		faqService.deleteFaq(faqId);
		return new ResponseEntity<Long>(faqId, HttpStatus.OK);
	}
	
}
