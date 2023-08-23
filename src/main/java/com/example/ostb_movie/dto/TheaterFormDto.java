package com.example.ostb_movie.dto;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.ostb_movie.entity.*;

import lombok.*;

@Getter
@Setter
public class TheaterFormDto {
	private Long id;

	private String theaterInfo; // 상영관 정보

	private int capacity; // 수용인원

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startTime; // 상영 시작 시간

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endTime; // 상영종료 시간

	private String age; // 연령등급

	private Movie movie;

	private static ModelMapper modelMapper = new ModelMapper();

	public Theater createTheater() {
		return modelMapper.map(this, Theater.class);
	}

	public static TheaterFormDto of(Theater theater) {
		return modelMapper.map(theater, TheaterFormDto.class);

	}

}
