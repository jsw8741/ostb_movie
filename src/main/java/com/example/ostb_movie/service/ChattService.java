package com.example.ostb_movie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.ostb_movie.constant.RoomStatus;
import com.example.ostb_movie.dto.FaqFormDto;
import com.example.ostb_movie.dto.OneBoardFormDto;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.OneBoard;
import com.example.ostb_movie.repository.MemberRepository;
import com.example.ostb_movie.repository.OneBoardRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChattService {

	private final MemberRepository memberRepository;
	private final OneBoardRepository oneBoardRepository;

	// 1:1 테이블에 방목록등록(insert)
	public Long saveOneBoard(OneBoardFormDto oneBoardFormDto, String email) {
		Member member = memberRepository.findByEmail(email);
		OneBoard oneBoard = oneBoardFormDto.createOne(member);
		oneBoardRepository.save(oneBoard);
		return oneBoard.getId();
	}

	// (관리자) 1:1 방 목록 보기
	@Transactional(readOnly = true)
	public Page<OneBoard> getOneList(Pageable pageable) {
		Page<OneBoard> oneBoard = oneBoardRepository.findAllByOrderByRoomStatusDescRegTimeDesc(pageable);
		return oneBoard;
	}

	// 1:1 방들어가기
	public Long enterOne(OneBoardFormDto oneBoardFormDto) {
		OneBoard oneBoard = oneBoardRepository.findById(oneBoardFormDto.getId())
				.orElseThrow(EntityNotFoundException::new);
		oneBoard.getOne(oneBoardFormDto);
		return oneBoard.getId();
	}

	// 나의 1:1 방 보기
	@Transactional(readOnly = true)
	public List<OneBoard> getOneBoard(long memberId) {
		return oneBoardRepository.getmyChatt(memberId);
	}

	// 1:1방 생성자 이름 가져오기
	public String getCreatedBy(String roomId) {
		return oneBoardRepository.getCreatedBy(roomId);
	}


	
	// 채팅창 끌때 진행중으로 상태 변경
	public void updateChatt(@PathParam("roomId") String roomId) {
		OneBoard oneBoard = oneBoardRepository.getId(roomId);
		oneBoard.setRoomStatus(RoomStatus.ONGOING);
		oneBoard.setModifiedBy(oneBoard.getCreatedBy());
	}
	
	// 채팅 종료시 종료로 상태변경
	public void closeChatt(@PathParam("roomId") String roomId) {
		OneBoard oneBoard = oneBoardRepository.getId(roomId);
		oneBoard.setRoomStatus(RoomStatus.CLOSE);
	}
	
	
	

}
