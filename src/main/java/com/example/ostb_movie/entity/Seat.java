package com.example.ostb_movie.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

	// private String type; // 좌석타입

	// private int price; // 가격

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Theater theater;

}
