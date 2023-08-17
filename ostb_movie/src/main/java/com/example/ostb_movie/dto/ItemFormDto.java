package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.constant.Categori;
import com.example.ostb_movie.entity.Item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemFormDto {
	private Long id;

	@NotBlank(message = "상품명은 필수 입력입니다.")
	private String itemNm;

	@NotNull(message = "가격은 필수 입력입니다.")
	private int price;

	@NotBlank(message = "상품 상세설명은 필수 입력입니다.")
	private String itemDetail;

	private Categori categori;

	private ItemImgDto itemImgDto;

	private static ModelMapper modelMapper = new ModelMapper();

	public Item createItem() {
		return modelMapper.map(this, Item.class);
	}

	public static ItemFormDto of(Item room) {
		return modelMapper.map(room, ItemFormDto.class);
	}

}
