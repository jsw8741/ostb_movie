package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Seat;
import com.example.ostb_movie.entity.Theater;

import lombok.*;

@Getter
@Setter
public class SeatDto {
	private Long id;
	private String name; // 좌석이름
	private String type; // 좌석타입
	private int price; // 좌석가격
	private Theater theater; // 상영관정보

	private static ModelMapper modelMapper = new ModelMapper();

	public Seat createSeat() {
		return modelMapper.map(this, Seat.class);
	}

	public static SeatDto of(Seat seat) {
		return modelMapper.map(seat, SeatDto.class);
	}
}
