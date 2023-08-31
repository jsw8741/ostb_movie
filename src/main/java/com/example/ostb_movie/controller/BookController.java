package com.example.ostb_movie.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	private final BookingService bookingService;
	private final SeatService seatService;

	// 상영시간표 페이지
	@GetMapping(value = "/book/schedule")
	public String schedule(Model model, @RequestParam(required = false) String viewDate) {
		LocalDate date;

		// viewDate 파라미터가 있으면 그 값 사용
		if (viewDate != null) {
			date = LocalDate.parse(viewDate);
		} else {
			// 없으면 현재 날짜 사용
			date = LocalDate.now();
		}

		// 상영스케줄 조회하고 조회 결과 모델에 추가
		List<Theater> schedules = theaterService.getTheaterSchedulesByDate(date, true);
		model.addAttribute("schedules", schedules);
		return "book/schedule";
	}

	// 상영정보 등록 페이지
	@GetMapping(value = "/book/theater/new")
	public String theaterForm(Model model) {
		List<Movie> movies = movieService.getMovieAll();
		model.addAttribute("movies", movies);
		model.addAttribute("theaterFormDto", new TheaterFormDto());
		return "book/theaterForm";
	}

	// 상영정보 등록 - insert
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

	// 상영정보 수정 페이지
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

	// 상영정보 관리 페이지
	@GetMapping(value = "/book/theaterMng")
	public String theaterMng(@RequestParam(required = false) LocalDate viewDate, Model model) {
		if (viewDate == null) {
			viewDate = LocalDate.now();
		}

		List<Theater> schedules = theaterService.getTheaterSchedulesByDate(viewDate, true);
		model.addAttribute("schedules", schedules);
		model.addAttribute("viewDate", viewDate);
		return "book/theaterMng";
	}

	// 상영정보 수정 - update
	@PostMapping(value = "/book/theater/{theaterId}")
	public String theaterUpdate(TheaterFormDto theaterFormDto, Model model) {
		try {
			theaterService.updateTheater(theaterFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상영정보 수정 중 에러가 발생했습니다.");
			return "book/theaterModifyForm";
		}
		return "redirect:/";
	}

	// 상영정보 삭제 - delete
	@GetMapping(value = "/book/theater/delete/{theaterId}")
	public String deleteTheater(@PathVariable("theaterId") Long theaterId) {
		theaterService.deleteTheater(theaterId);
		return "redirect:/book/theaterMng";
	}

	// 티켓예매 페이지
	@GetMapping(value = "/book/ticketing")
	public String ticketing(Model model, @RequestParam(required = false) String viewDate) {
		LocalDate date;

		// viewDate 파라미터가 있으면 그 값 사용
		if (viewDate != null) {
			date = LocalDate.parse(viewDate);
		} else {
			// 없으면 현재 날짜 사용
			date = LocalDate.now();
		}

		List<Theater> schedules = theaterService.getTheaterSchedulesByDate(date, true);
		model.addAttribute("schedules", schedules);
		return "book/ticketing";
	}

	// 티켓예매
	@PostMapping(value = "/book/tickeing")
	@ResponseBody
	public Map<String, Object> submitTicketing(@RequestBody Map<String, Object> paramMap, Model model,
			Authentication authentication) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			// 예매정보 처리
			bookingService.submitTicketing(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	// 좌석정보 조회
	@PostMapping(value = "/book/getSeatData")
	@ResponseBody
	public Map<String, Object> getSeatData(@RequestBody Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 상영관의 좌석 예약 정보 조회
			List<String> list = seatService.getSeatList(paramMap.get("theaterId").toString());
			resultMap.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	// 결제처리
	@PostMapping(value = "/book/payment")
	@ResponseBody
	public Map<String, Object> submitTicketing(@RequestBody Map<String, Object> paramMap, Model model) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 결제정보 저장 처리 로직
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	// 예매내역 페이지
	@GetMapping(value = "/book/history")
	public String history() {
		return "book/history";
	}
}