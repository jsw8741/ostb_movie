package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Itemimg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {
	private Long id;

	private String imgName;

	private String oriImgName;

	private String imgUrl;

	private static ModelMapper modelMapper = new ModelMapper();

	public static ItemImgDto of(Itemimg itemImg) {
		return modelMapper.map(itemImg, ItemImgDto.class);
	}
}
