package com.example.ostb_movie.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.ostb_movie.dto.TheaterFormDto;
import com.example.ostb_movie.entity.*;
import com.example.ostb_movie.service.*;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookController {
	private final TheaterService theaterService;
	private final MovieService movieService;

	// 상영시간표 페이지
	@GetMapping(value = "/book/schedule")
	public String schedule(Model model, @RequestParam(required = false) String viewDate) {
		LocalDate date;

		if (viewDate != null) {
			date = LocalDate.parse(viewDate);
		} else {
			date = LocalDate.now();
		}

		List<Theater> schedules = theaterService.getTheaterSchedulesByDate(date);

		System.out.println(schedules);
		model.addAttribute("schedules", schedules);
		return "book/schedule";
	}

	// 상영 정보 등록 페이지
	@GetMapping(value = "/book/theater/new")
	public String theaterForm(Model model) {
		List<Movie> movies = movieService.getMovieAll();
		model.addAttribute("movies", movies);
		model.addAttribute("theaterFormDto", new TheaterFormDto());
		return "book/theaterForm";
	}

	// 상영 정보 등록 - insert
	@PostMapping(value = "/book/theater/new")
	public String submitTheaterForm(TheaterFormDto theaterFormDto, Model model) {
		try {
			theaterService.saveTheater(theaterFormDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "상영정보 등록 중 에러가 발생했습니다.");
			return "book/theaterForm";
		}
		return "redirect:/";
	}

	// 상영 정보 수정 페이지
	@GetMapping(value = "/book/theater/{theaterId}")
	public String theaterDtl(@PathVariable("theaterId") Long theaterId, Model model) {

		try {
			List<Movie> movies = movieService.getMovieAll();
			TheaterFormDto theaterFormDto = theaterService.getTheaterDtl(theaterId);
			model.addAttribute("movies", movies);
			model.addAttribute("theaterFormDto", theaterFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상영정보를 가져오는 도중 에러가 발생했습니다.");
			model.addAttribute("theaterFormDto", new TheaterFormDto());
			return "book/theaterForm";
		}

		return "book/theaterModifyForm";
	}

	// 상영 정보 수정 - update
	@PostMapping(value = "/book/theater/{theaterId}")
	public String theaterUpdate(@ModelAttribute("theaterFormDto") TheaterFormDto theaterFormDto, Model model) {
		try {
			theaterService.updateTheater(theaterFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상영정보 수정 중 에러가 발생했습니다.");
			return "book/theaterModifyForm";
		}
		return "redirect:/"; // 수정 성공 시 메인 페이지로 이동
	}

	// 상영 정보 관리 페이지
	@GetMapping(value = "/book/theaterMng")
	public String theaterMng() {

		return "book/theaterMng";

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
