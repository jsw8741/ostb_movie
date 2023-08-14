package com.example.ostb_movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	@GetMapping("/login/loginForm")
	public String loginForm() {
		return "/login/loginForm";
	}
	
	@GetMapping("/login/joinForm")
	public String joinForm() {
		return "/login/joinForm";
	}
	
}
