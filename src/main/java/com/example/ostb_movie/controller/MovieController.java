package com.example.ostb_movie.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ostb_movie.entity.MovieStatus;
import com.example.ostb_movie.service.ApiService;
import com.example.ostb_movie.service.MovieStatusService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MovieController {
	private final ApiService apiService;
	private final MovieStatusService movieStatusService;
	
	@GetMapping(value = "/")
	public String movieHome(Model model, Authentication authentication){
		
		// 로그인되지 않은 상태
		if (authentication == null || !authentication.isAuthenticated()) {

			if(movieStatusService.getStatusList().isEmpty()) {
				MovieStatus popular = MovieStatus.createMovieStatus("인기순위");
				MovieStatus upComing = MovieStatus.createMovieStatus("개봉예정");
				MovieStatus nowPlaying = MovieStatus.createMovieStatus("상영중");
				
				movieStatusService.saveMovieStatus(popular);
				movieStatusService.saveMovieStatus(upComing);
				movieStatusService.saveMovieStatus(nowPlaying);
			}
			
		}
		
		return "movieHome";
	}
	
	@ResponseBody
	@GetMapping("/api/getInfo")
	public String getInfo() {
		try {

            apiService.getPopular();
	            
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return "ok";
	}
}
