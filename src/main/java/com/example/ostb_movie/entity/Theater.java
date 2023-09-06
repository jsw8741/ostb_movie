package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.dto.TheaterFormDto;

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

	private String age; // 연령등급

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id") // 해당하는 상영관과 연결된 영화
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Movie movie;

	public void updateTheater(TheaterFormDto theaterFormDto) {
		this.theaterInfo = theaterFormDto.getTheaterInfo();
		this.capacity = theaterFormDto.getCapacity();
		this.startTime = theaterFormDto.getStartTime();
		this.endTime = theaterFormDto.getEndTime();
		this.age = theaterFormDto.getAge();
		this.movie = theaterFormDto.getMovie();
	}
}