package com.example.ostb_movie.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.OneBoardFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.service.ChattService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
		mv.setViewName("chatt/chatting");
		mv.addObject("session", session);
		return mv;
	}
	

	
}
