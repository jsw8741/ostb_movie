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
	public List<Theater> getTheaterSchedulesByDate(LocalDate date, Boolean state) {
		LocalDate now = LocalDate.now();
		LocalDateTime start = LocalDateTime.of(now, LocalTime.now());
		LocalDateTime end = LocalDateTime.of(now.plusDays(30), LocalTime.now());
		
		if(state) {
			start = date.atStartOfDay();
			end = date.atTime(23, 59, 59);
		}
		
		Sort sort = Sort.by(Sort.Direction.ASC, "startTime");
		return theaterRepository.findByStartTimeBetween(start, end, sort);
	}

	// 상영정보 등록
	public Long saveTheater(TheaterFormDto theaterFormDto) throws Exception {
		Theater theater = theaterFormDto.createTheater();
		theaterRepository.save(theater);
		return theater.getId();
	}
	
	// 상영정보
	@Transactional(readOnly = true)
	public TheaterFormDto getTheaterDtl(Long theaterId) {
		Theater theater = theaterRepository.findById(theaterId).orElseThrow(EntityNotFoundException::new);
		TheaterFormDto theaterFormDto = TheaterFormDto.of(theater);
		return theaterFormDto;
	}

	// 상영정보 수정
	@Transactional
	public void updateTheater(TheaterFormDto theaterFormDto) {
		Theater theater = theaterRepository.findById(theaterFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		theater.updateTheater(theaterFormDto);
		theaterRepository.save(theater);
	}

	// 상영정보 삭제
	@Transactional
	public void deleteTheater(Long theaterId) {
		theaterRepository.deleteById(theaterId);
	}
	
	//  상영 정보 가져오기
	public Theater getTheater(Long theaterId) {
		return theaterRepository.findById(theaterId).orElseThrow(EntityNotFoundException::new);
	}
};