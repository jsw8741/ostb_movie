package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.constant.OrderStatus;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Member;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {

	private Item itemId;

	private String imgUrl;

	private String totalprice;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int count;
}
