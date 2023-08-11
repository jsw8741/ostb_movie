package com.example.ostb_movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MovieController {
	@GetMapping(value = "/")
	public String movieHome(){
		
		return "movieHome";
	}
}
