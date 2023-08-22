package com.example.ostb_movie.controller;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.ostb_movie.entity.Movie;
import com.example.ostb_movie.entity.MovieStatus;
import com.example.ostb_movie.service.MovieService;
import com.example.ostb_movie.service.MovieStatusService;

import lombok.RequiredArgsConstructor;

@Controller
@Component
@RequiredArgsConstructor
public class MovieController {
	private final MovieService movieService;
	private final MovieStatusService movieStatusService;
	
	@GetMapping(value = "/")
	public String movieHome(Model model, Authentication authentication){
		
		// 로그인되지 않은 상태
		if (authentication == null || !authentication.isAuthenticated()) {

			if(movieStatusService.getStatusList().isEmpty()) {
				MovieStatus popular = MovieStatus.createMovieStatus((long) 1, "인기순위");
				MovieStatus upComing = MovieStatus.createMovieStatus((long) 2, "개봉예정");
				MovieStatus nowPlaying = MovieStatus.createMovieStatus((long) 3, "상영중");
				
				movieStatusService.saveMovieStatus(popular);
				movieStatusService.saveMovieStatus(upComing);
				movieStatusService.saveMovieStatus(nowPlaying);
			}
			
			if(movieService.getMovieAll().isEmpty()) {
				movieService.getPopular();
				movieService.getUpComing();
				movieService.getNowPlaying();
			}
			
		}
		List<Movie> moviePopualrList = movieService.getMoviePopular();
		
		model.addAttribute("moviePopualrList", moviePopualrList);
		
		return "movieHome";
	}
	
	// 매 주 월요일 오전 00:00에 DB 업데이트
	@Scheduled(cron = "0 0 0 ? * 1")
    public void updateMovieDB() {
		movieService.updateMovie();
    }
	
	
}
