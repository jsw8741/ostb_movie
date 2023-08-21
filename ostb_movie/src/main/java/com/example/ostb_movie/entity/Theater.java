package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theater")
@Getter
@Setter
@ToString
public class Theater {
	@Id
	@Column(name = "theater_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String theaterNm; //상영관정보
	
	private int capacity; //수용인원
	
	private LocalDateTime showDate; //상영날짜
	
	private LocalDateTime startTime; //상영시작시간
	
	private LocalDateTime endTime; //상영종료시간
	
	/*
	@ManyToOne
	@JoinColumn(name = "movie_detail_id")
	private MovieDetail movieDetail;
	*/

}
