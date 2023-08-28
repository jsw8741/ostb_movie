package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewModifyDto {
	private Long id;
	
	private String content;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ReviewModifyDto of (Review review) {
		return modelMapper.map(review, ReviewModifyDto.class);
	}
}
