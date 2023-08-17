package com.example.ostb_movie.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.entity.MovieStatus;
import com.example.ostb_movie.repository.MovieStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieStatusService {
	private final MovieStatusRepository movieStatusRepository;

	public List<MovieStatus> getStatusList(){
		List<MovieStatus> movieStatusList = movieStatusRepository.findAll();
		
		return movieStatusList;
	}
	
	public MovieStatus saveMovieStatus(MovieStatus movieStatus) {
		MovieStatus saveMovieStatus = movieStatusRepository.save(movieStatus);
		
		return saveMovieStatus;
	}
}
