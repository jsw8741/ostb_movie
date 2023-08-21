package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theater")
@Getter
@Setter
@ToString
public class Theater extends BaseEntity {
	@Id
	@Column(name = "theater_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String theaterInfo; // 상영관정보

	private int capacity; // 수용인원

	private LocalDateTime startTime; // 상영시작시간

	private LocalDateTime endTime; // 상영종료시간

	@ManyToOne
	@JoinColumn(name = "movie_detail_id") // 해당하는 상영관과 연결된 영화 정보
	private MovieDetail movieDetail;
}