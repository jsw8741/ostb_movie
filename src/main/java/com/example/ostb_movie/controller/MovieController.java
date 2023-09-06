package com.example.ostb_movie.controller;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.ostb_movie.dto.ReviewDto;
import com.example.ostb_movie.entity.Itemimg;
import com.example.ostb_movie.entity.Movie;
import com.example.ostb_movie.entity.MovieStatus;
import com.example.ostb_movie.entity.Review;
import com.example.ostb_movie.service.ItemImgService;
import com.example.ostb_movie.service.MovieService;
import com.example.ostb_movie.service.MovieStatusService;
import com.example.ostb_movie.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@Component
@RequiredArgsConstructor
public class MovieController {
	private final MovieService movieService;
	private final MovieStatusService movieStatusService;
	private final ReviewService reviewService;
	private final ItemImgService itemImgService;
	
	@GetMapping(value = "/")
	public String movieHome(Model model, Authentication authentication){
		List<Itemimg> items = itemImgService.allItemList();
		model.addAttribute("items", items);
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
	
	@GetMapping(value = "/movie/chart")
	public String movieChart(Model model) {
		
		List<Movie> popularList = movieService.getMoviePopular();
		List<Movie> nowPlayingList = movieService.getMovieNowPlaying();
		List<Movie> upCommingList = movieService.getMovieUpComming();
		
		model.addAttribute("popularList", popularList);
		model.addAttribute("nowPlayingList", nowPlayingList);
		model.addAttribute("upCommingList", upCommingList);
		
		return "movie/movieChart";
	}
	
	// 매 주 월요일 오전 00:00에 DB 업데이트
	@Scheduled(cron = "0 0 0 ? * 1")
    public void updateMovieDB() {
		movieService.updateMovie();
    }
	
	@GetMapping(value = "/movie/detail/{originId}")
	public String movieDtl(@PathVariable("originId") Long originId, Model model) {
		
		Movie movieDtl = movieService.getMovieDtl(originId);
		
		
		reviewService.getMovieReviewAll(originId);
		
		
		model.addAttribute("movieDtl", movieDtl);
		model.addAttribute("reviewDto", new ReviewDto());
		
		return "movie/movieDtl";
	}
	
	
	
}
