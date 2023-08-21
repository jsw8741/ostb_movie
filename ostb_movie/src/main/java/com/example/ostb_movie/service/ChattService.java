package com.example.ostb_movie.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.dto.FaqFormDto;
import com.example.ostb_movie.dto.OneBoardFormDto;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.OneBoard;
import com.example.ostb_movie.repository.MemberRepository;
import com.example.ostb_movie.repository.OneBoardRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChattService {

	private final MemberRepository memberRepository;
	private final OneBoardRepository oneBoardRepository;
	
	//1:1 테이블에 방목록등록(insert)
	public Long saveOneBoard(OneBoardFormDto oneBoardFormDto, String email) {
		Member member = memberRepository.findByEmail(email);
		OneBoard oneBoard = oneBoardFormDto.createOne(member);
		oneBoardRepository.save(oneBoard);
		return oneBoard.getId();
	}
	
	
	//1:1 방목록 가져오기
	@Transactional(readOnly = true)
	public Page<OneBoard> getOneList(Pageable pageable){
		Page<OneBoard> oneBoard = oneBoardRepository.findAllByOrderByIdDesc(pageable);
		return oneBoard;
	}
	
	//1:1 방들어가기
	public Long enterOne(OneBoardFormDto oneBoardFormDto) {
		OneBoard oneBoard = oneBoardRepository.findById(oneBoardFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		oneBoard.getOne(oneBoardFormDto);
		return oneBoard.getId();
	}
	
	
	
	
	
	
}
