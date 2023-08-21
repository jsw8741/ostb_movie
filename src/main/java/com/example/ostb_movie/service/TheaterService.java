package com.example.ostb_movie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.dto.TheaterFormDto;
import com.example.ostb_movie.entity.Theater;
import com.example.ostb_movie.repository.TheaterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TheaterService {
	private final TheaterRepository theaterRepository;

	public Long saveTheater(TheaterFormDto theaterFormDto) throws Exception {
		Theater theater = theaterFormDto.createTheater();
		theaterRepository.save(theater);

		return theater.getId();
	}
}
