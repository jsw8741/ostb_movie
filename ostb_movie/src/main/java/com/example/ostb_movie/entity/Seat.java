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
	
	private String seatNm; //좌석이름
	
	private String seatType; //좌석타입
	
	private int price; //가격
	
	private int count; //수량
	
	@ManyToOne
	@JoinColumn(name = "theater_id")
	private Theater theater;
}