package com.example.ostb_movie.dto;

import com.example.ostb_movie.entity.Payment;
import com.example.ostb_movie.entity.Seat;

import lombok.*;

@Getter
@Setter
public class BookDto {
	private Long id; 
	private String bookNo; // 예매번호 
	private int totalPrice; // 총금액
	private Seat seat; // 좌석정보
	private Payment payment; // 결제정보
}
