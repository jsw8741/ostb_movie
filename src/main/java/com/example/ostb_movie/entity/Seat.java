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

	private String seatType; // 좌석타입

	private int price; // 좌석가격

	@ManyToOne
<<<<<<< HEAD:src/main/java/com/example/ostb_movie/entity/Seat.java
	@JoinColumn(name = "theater_id") // 해당하는 좌석이 위치한 상영관 정보
=======
	@JoinColumn(name = "theater_id")
	@OnDelete(action= OnDeleteAction.CASCADE)
>>>>>>> 3c2f6fd64386dc4134b8b47b63392128975d35de:ostb_movie/src/main/java/com/example/ostb_movie/entity/Seat.java
	private Theater theater;
}