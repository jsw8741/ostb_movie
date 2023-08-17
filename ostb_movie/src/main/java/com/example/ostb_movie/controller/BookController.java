package com.example.ostb_movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {
	// 상영스케줄 페이지
	@GetMapping(value = "/book/timetable")
	public String timetable() {
		return "book/timetable";
	}
	
	// 예매내역 페이지
	@GetMapping(value = "/book/history")
	public String history() {
		return "book/bookHist";
	}
	
	// 티켓예매 페이지
	@GetMapping(value = "/book/ticketing")
	public String ticketing() {
		return "book/ticket";
	}
}
