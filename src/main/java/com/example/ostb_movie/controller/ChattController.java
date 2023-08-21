package com.example.ostb_movie.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChattController {
	
	@RequestMapping("/chatt/{roomId}")
	public ModelAndView chatt(@PathVariable String roomId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("roomId", roomId);
		mv.setViewName("chatt/chatting");
		return mv;
	}
}
