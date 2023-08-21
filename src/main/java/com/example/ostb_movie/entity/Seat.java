package com.example.ostb_movie.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat")
@Getter
@Setter
@ToString
public class Seat {
	@Id
	@Column(name = "seat_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String seatNm; // 좌석이름

	private String seatType; // 좌석타입

	private int price; // 좌석가격

	@ManyToOne
	@JoinColumn(name = "theater_id") // 해당하는 좌석이 위치한 상영관 정보
	private Theater theater;
}