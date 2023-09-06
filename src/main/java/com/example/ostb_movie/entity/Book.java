package com.example.ostb_movie.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.constant.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {
	@Id
	@Column(name = "book_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String bookNo; // 예매번호

	private int discount; // 할인

	private int price; // 금액

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Seat seat;

	// 할인 적용된 총금액
	public int calculateTotalPrice() {
		int totalPrice = this.price;

		totalPrice -= this.discount;

		if (totalPrice < 0) {
			totalPrice = 0;
		}

		return totalPrice;
	}

}