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
import com.example.ostb_movie.dto.LostFormDto;
import com.example.ostb_movie.entity.Lost;
import com.example.ostb_movie.repository.LostRepository;
import com.example.ostb_movie.service.LostService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LostController {
	private final LostService lostService;
	private final LostRepository lostRepository;

	// lost 생성페이지
	@GetMapping(value = "/lost/createLost")
	public String lostForm(Model model) {
		model.addAttribute("lostFormDto", new LostFormDto());
		return "lost/createLost";
	}

	// lost 등록(insert)
	@PostMapping(value = "/lost/createLost")
	public String lostNew(@Valid LostFormDto lostFormDto, BindingResult bindingResult, Model model, Principal principal) {

		if (bindingResult.hasErrors()) {
			return "/lost/createLost";
		}

		try {
			String email = principal.getName();
			lostService.saveLost(lostFormDto, email);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ등록 중 에러가 발생했습니다.");
			return "/lost/createLost";
		}
		return "redirect:/lost/list";
	}

	// lost 리스트
	@GetMapping(value = { "/lost/list", "/lost/list/{page}" })
	public String faqMainList(Model model, @PathVariable("page") Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<Lost> losts = lostService.getMainLostDtl(pageable);
		Long totalCount = lostRepository.count();

		model.addAttribute("losts", losts);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("maxPage", 5);

		return "lost/listLost";
	}

	// lost 수정페이지 보기
	@GetMapping(value = "/lost/updateLost/{lostId}")
	public String lostDtl(@PathVariable("lostId") Long lostId, Model model) {

		try {
			Lost lost = lostRepository.findById(lostId).orElseThrow(EntityNotFoundException::new);
			model.addAttribute("lost", lost);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ정보를 가져올때 에러가 발생했습니다.");
			model.addAttribute("lost", new Lost());
			return "lost/listLost";
		}
		return "lost/updateLost";
	}

	// lost 수정(update)
	@PostMapping(value = "/lost/updateLost/{lostId}")
	public String lostUpdate(@Valid LostFormDto lostFormDto, Model model, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "lost/list";
		}

		try {
			lostService.updateLost(lostFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "FAQ 수정 중 에러가 발생했습니다.");
			
			return "lost/updateLost";
		}
		return "redirect:/lost/updateLost/{lostId}";
	}

	// lost 삭제(delete)
	@DeleteMapping("/lost/deleteLost/{lostId}/delete")
	public @ResponseBody ResponseEntity deleteLost(@PathVariable("lostId") Long lostId, Principal principal) {

		// 1. 본인인증
		if (!lostService.validateFaq(lostId, principal.getName())) {
			return new ResponseEntity<String>("lost 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}

		lostService.deleteLost(lostId);
		return new ResponseEntity<Long>(lostId, HttpStatus.OK);
	}

}
