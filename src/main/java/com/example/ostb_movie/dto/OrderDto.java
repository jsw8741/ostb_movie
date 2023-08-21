package com.example.ostb_movie.dto;

import com.example.ostb_movie.constant.OrderStatus;
import com.example.ostb_movie.entity.Item;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

	private Item itemId;

	private String imgUrl;

	private String totalprice;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int count;
}
