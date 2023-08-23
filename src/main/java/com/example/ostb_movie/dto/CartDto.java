package com.example.ostb_movie.dto;


import com.example.ostb_movie.constant.CartStatus;
import com.example.ostb_movie.entity.Item;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {

	private Item itemId;

	private String imgUrl;

	private String totalprice;

	@Enumerated(EnumType.STRING)
	private CartStatus CartStatus;

	private int count;
}
