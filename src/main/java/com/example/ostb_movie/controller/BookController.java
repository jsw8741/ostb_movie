package com.example.ostb_movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.ostb_movie.dto.TheaterFormDto;
import com.example.ostb_movie.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookController {
	private final TheaterService theaterService;

	// 상영시간표 페이지
	@GetMapping(value = "/book/schedule")
	public String schedule() {
		return "book/schedule";
	}

	// 상영관, 영화 등록 페이지
	@GetMapping(value = "/book/theaterForm")
	public String theaterForm(Model model) {
		model.addAttribute("theaterFormDto", new TheaterFormDto());
		return "book/theaterForm";
	}

	// 상영관, 영화 등록
	@PostMapping(value = "/book/theaterForm/new")
	public String theaterNew(@Valid TheaterFormDto theaterFormDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "book/theaterForm";
		}

		try {
			theaterService.saveTheater(theaterFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "등록 중 에러가 발생했습니다.");
			return "book/theaterForm";
		}

		return "redirect:/";
	}

	// 예매내역 페이지
	@GetMapping(value = "/book/history")
	public String history() {
		return "book/history";
	}

	// 티켓예매 페이지
	@GetMapping(value = "/book/ticketing")
	public String ticketing() {
		return "book/ticketing";
	}
}
