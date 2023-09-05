package com.example.ostb_movie.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.FaqFormDto;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.service.FaqService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FaqController {
	private final FaqService faqService;
	

	//faq 생성페이지
	@GetMapping(value = "/FAQ/createFAQ")
	public String faqForm(Model model) {
		model.addAttribute("faqFormDto", new FaqFormDto());
		return "FAQ/createFAQ";
	}
	
	//faq 등록(insert)
	@PostMapping(value = "/FAQ/createFAQ")
	public String faqNew(@Valid FaqFormDto faqFormDto, BindingResult bindingResult,
			Model model, Authentication authentication) {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
		if(bindingResult.hasErrors()) {
			return "FAQ/createFAQ";
		}
		
		try {
		String email = member.getEmail();
		faqService.saveFaq(faqFormDto, email);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ등록 중 에러가 발생했습니다.");
			return "FAQ/createFAQ";
		}
		return "redirect:/FAQ/list"; 
	}
	
	// faq 리스트
	@GetMapping(value = { "/FAQ/list", "/FAQ/list/{page}" } )
	public String faqMainList(Model model,@PathVariable("page") Optional<Integer> page, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member currentMember = principal.getMember();
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Faq> faqs = faqService.getMainFaqDtl(pageable);
		Long totalCount = faqService.getTotalFaqcount();
		
		model.addAttribute("currentMember", currentMember);
		model.addAttribute("faqs", faqs);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "FAQ/listFAQ";
	}
	
	//faq 수정페이지 보기
	@GetMapping(value = "/FAQ/updateFAQ/{faqId}")
	public String faqDtl(@PathVariable("faqId") Long faqId, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
		
        if(faqService.validateFaq(faqId, member.getEmail())) {
        	try {
    			Faq faq = faqService.getModifFaq(faqId);
    			model.addAttribute("faq", faq);
    		} catch (Exception e) {
    			e.printStackTrace();
    			model.addAttribute("errorMessage", "FAQ정보를 가져올때 에러가 발생했습니다.");
    			return "redirect:/FAQ/list";
    		}       	
        }else {
        	redirectAttributes.addFlashAttribute("errorMessage", "작성자 본인만 수정이 가능합니다.");
        	return "redirect:/FAQ/list";
        }
        
		
		
		return "FAQ/updateFAQ";
	}
	
	//faq 수정(update)
	@PostMapping(value = "/FAQ/updateFAQ/{faqId}" )
	public String faqUpdate(@Valid FaqFormDto faqFormDto, Model model, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "FAQ/listFAQ";
		}
		
		try {
			faqService.updateFaq(faqFormDto);
			return "redirect:/FAQ/list"; 
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ 수정 중 에러가 발생했습니다.");
			return "FAQ/updateFAQ";
		}
		
	}
	
	// faq 삭제(delete)
	@DeleteMapping("/FAQ/deleteFAQ/{faqId}/delete")
	public @ResponseBody ResponseEntity deleteFaq(@PathVariable("faqId") Long faqId, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        
		//1. 본인인증
		if(!faqService.validateFaq(faqId, member.getEmail())) {
			return new ResponseEntity<String>("FAQ 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		faqService.deleteFaq(faqId);
		return new ResponseEntity<Long>(faqId, HttpStatus.OK);
	}
	
	// faq 회원
	@GetMapping(value = { "/FAQ/list/member", "/FAQ/list/member/{page}" } )
	public String faqMemberlist(Model model,@PathVariable("page") Optional<Integer> page, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member currentMember = principal.getMember();
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Faq> faqs = faqService.getMainFaqDtl(pageable);
		Long totalCount = faqService.getTotalFaqcount();
		
		model.addAttribute("currentMember", currentMember);
		model.addAttribute("faqs", faqs);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "FAQ/faqMember";
	}
	
	// faq 포인트
	@GetMapping(value = { "/FAQ/list/point", "/FAQ/list/point/{page}" } )
	public String faqPointlist(Model model,@PathVariable("page") Optional<Integer> page, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member currentMember = principal.getMember();
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Faq> faqs = faqService.getMainFaqDtl(pageable);
		Long totalCount = faqService.getTotalFaqcount();
		
		model.addAttribute("currentMember", currentMember);
		model.addAttribute("faqs", faqs);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "FAQ/faqPoint";
	}
	
	// faq 혜택
	@GetMapping(value = { "/FAQ/list/benefit", "/FAQ/list/benefit/{page}" } )
	public String faqBenefitlist(Model model,@PathVariable("page") Optional<Integer> page, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member currentMember = principal.getMember();
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Faq> faqs = faqService.getMainFaqDtl(pageable);
		Long totalCount = faqService.getTotalFaqcount();
		
		model.addAttribute("currentMember", currentMember);
		model.addAttribute("faqs", faqs);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "FAQ/faqBenefit";
	}
	
	// faq 친구
	@GetMapping(value = { "/FAQ/list/frend", "/FAQ/list/frend/{page}" } )
	public String faqFrendlist(Model model,@PathVariable("page") Optional<Integer> page, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member currentMember = principal.getMember();
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Faq> faqs = faqService.getMainFaqDtl(pageable);
		Long totalCount = faqService.getTotalFaqcount();
		
		model.addAttribute("currentMember", currentMember);
		model.addAttribute("faqs", faqs);
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("maxPage", 5);
		
		return "FAQ/faqFrend";
	}
	
	
	
	
	
	
	
}
