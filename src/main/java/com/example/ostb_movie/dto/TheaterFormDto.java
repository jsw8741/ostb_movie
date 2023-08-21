package com.example.ostb_movie.dto;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.*;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class TheaterFormDto {
	private Long id;

	@NotBlank(message = "상영관을 입력해 주세요.")
	private String theaterNm;

	@NotNull(message = "수용인원을 입력해 주세요.")
	private int capacity;

	@NotNull(message = "상영 시작시간을 입력해 주세요.")
	private LocalDateTime startTime;

	@NotNull(message = "상영 종료시간을 입력해 주세요.")
	private LocalDateTime endTime;

	private MovieDetail movieDetail;
	
	private static ModelMapper modelMapper = new ModelMapper();

	public Theater createTheater() {
		return modelMapper.map(this, Theater.class);
	}

	public static TheaterFormDto of(Theater theater) {
		return modelMapper.map(theater, TheaterFormDto.class);
	}

}
