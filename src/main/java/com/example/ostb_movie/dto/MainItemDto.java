package com.example.ostb_movie.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
	private Long id;
	private String itemNm;
	private int price;
	private String itemDetail;
	private String imgUrl;

	@QueryProjection // 쿼리dsl로 결과 조회 할때 MainItemDto 객체로 바로 받아올 수 있다.
	public MainItemDto(Long id, String itemNm, String itemDetail, Integer price, String imgurl) {
		this.id = id;
		this.itemNm = itemNm;
		this.itemDetail = itemDetail;
		this.imgUrl = imgurl;
		this.price = price;
	}
}
