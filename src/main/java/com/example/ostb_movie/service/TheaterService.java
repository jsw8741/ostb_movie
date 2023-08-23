package com.example.ostb_movie.service;

import java.time.*;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.dto.TheaterFormDto;
import com.example.ostb_movie.entity.Theater;
import com.example.ostb_movie.repository.TheaterRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TheaterService {
	private final TheaterRepository theaterRepository;

	// 상영시간표
	public List<Theater> getTheaterSchedulesByDate(LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.atTime(23, 59, 59);
		Sort sort = Sort.by(Sort.Direction.ASC, "startTime");
		return theaterRepository.findByStartTimeBetween(start, end, sort);
	}

	// 상영 정보 등록
	public Long saveTheater(TheaterFormDto theaterFormDto) throws Exception {
		Theater theater = theaterFormDto.createTheater();
		theaterRepository.save(theater);
		return theater.getId();
	}

	@Transactional(readOnly = true)
	public TheaterFormDto getTheaterDtl(Long theaterId) {
		Theater theater = theaterRepository.findById(theaterId).orElseThrow(EntityNotFoundException::new);
		TheaterFormDto theaterFormDto = TheaterFormDto.of(theater);
		return theaterFormDto;
	}

	// 상영 정보 수정
	public Long updateTheater(TheaterFormDto theaterFormDto) {
		Theater theater = theaterRepository.findById(theaterFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		theater.updateTheater(theaterFormDto);
		return theater.getId();
	}

}