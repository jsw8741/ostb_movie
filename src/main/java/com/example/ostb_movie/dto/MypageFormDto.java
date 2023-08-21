package com.example.ostb_movie.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MypageFormDto {
	private Long id;
	
	private String name;
	
	private String email;
	
	private String imgUrl;
	
	private String nickname;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	private List<Long> memberImgIds = new ArrayList<>();
	
	public Member createMypage() {
		return modelMapper.map(this, Member.class);
	}

	public static MypageFormDto of (Member member) {
		return modelMapper.map(member, MypageFormDto.class);
	}
}
