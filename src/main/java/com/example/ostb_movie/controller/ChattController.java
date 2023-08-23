package com.example.ostb_movie.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.ostb_movie.service.ChattService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ChattController {
	
	private final ChattService chattService;
	
    public ChattController(ChattService chattService) {
        this.chattService = chattService;
    }
	
	
	@RequestMapping("/chatt/{roomId}")
	public ModelAndView chatt(@PathVariable String roomId, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		HttpSession session = request.getSession();
		session.setAttribute(roomId, "roomId");
		
		mv.addObject("roomId", roomId);
		mv.addObject("createdBy", chattService.getCreatedBy(roomId));
		System.out.println("sssssssssssssssss"+chattService.getCreatedBy(roomId));
		mv.setViewName("chatt/chatting");
		mv.addObject("session", session);
		return mv;
	}
}
