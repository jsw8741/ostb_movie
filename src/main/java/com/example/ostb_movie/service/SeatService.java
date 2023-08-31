package com.example.ostb_movie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {
	private final SeatRepository seatRepository;
	
	public List<String> getSeatList(String theaterId) {
		return seatRepository.getSeatList(theaterId);
		
	}
	
	

}
