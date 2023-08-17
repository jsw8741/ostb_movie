package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.constant.LostStatus;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Lost;
import com.example.ostb_movie.entity.Member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LostFormDto {
	
	private Long id;
	
	private LostStatus lostStatus;
	
	@NotBlank(message = "분실물 이름을 입력해주세요.")
	private String lostItem;
	
	@NotBlank(message = "분실물 내용을 입력해주세요.")
	private String lostDetail;
	
	private long memberId;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//dto -> entity로 바꿔줌
	public Lost createLost(Member member) {
		this.memberId = member.getId();
		lostStatus = LostStatus.ACCEPT;
		return modelMapper.map(this, Lost.class);
	}
	
	//entity를 dto로 바꿔줌
	public static LostFormDto of(Lost lost) {
		return modelMapper.map(lost, LostFormDto.class);
	}
	
}
