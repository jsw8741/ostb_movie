package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.constant.RoomStatus;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.OneBoard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OneBoardFormDto {
	
	private Long id;
	
	private long memberId;
	
	private String sessionId;
	
	private RoomStatus roomStatus;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//dto -> entity로 바꿔줌
	public OneBoard createOne(Member member) {
		this.memberId = member.getId();
		roomStatus = RoomStatus.OPEN;
		return modelMapper.map(this, OneBoard.class);
	}
	
	//entity를 dto로 바꿔줌
	public static OneBoardFormDto of(OneBoard oneBoard) {
		return modelMapper.map(oneBoard, OneBoardFormDto.class);
	}
	
	
	
	
	
	
}
