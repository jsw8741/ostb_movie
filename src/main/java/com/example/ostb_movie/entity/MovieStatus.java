package com.example.ostb_movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movie_status")
@Getter
@Setter
public class MovieStatus {
	@Id
	@Column(name="movie_status_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(nullable = false)
    private String status;
	
	public static MovieStatus createMovieStatus(String status) {
		MovieStatus movieStatus = new MovieStatus();
		
		movieStatus.status = status;
		
		return movieStatus;
	}
}
