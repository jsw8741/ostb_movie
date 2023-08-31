package com.example.ostb_movie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.dto.SeatDto;
import com.example.ostb_movie.entity.Seat;
import com.example.ostb_movie.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {
	private final SeatRepository seatRepository;

	public Long saveSeat(SeatDto seatDto) throws Exception {
		Seat seat = seatDto.createSeat();
		seatRepository.save(seat);
		return seat.getId();
	}
	
	public List<String> getSeatList(String theaterId) {
		return seatRepository.getSeatList(theaterId);
		
	}
	
	

}
