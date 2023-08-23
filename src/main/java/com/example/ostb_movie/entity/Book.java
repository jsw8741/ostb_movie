package com.example.ostb_movie.entity;

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

	private String bookNm; // 예매번호

	private String price; // 예매가격

	@ManyToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	@ManyToOne
	@JoinColumn(name = "seat_id")
	private Seat seat;
}