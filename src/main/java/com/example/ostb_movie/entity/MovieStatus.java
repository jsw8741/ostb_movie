package com.example.ostb_movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Long id;
	
	@Column(nullable = false)
    private String status;
	
	public static MovieStatus createMovieStatus(Long id, String status) {
		MovieStatus movieStatus = new MovieStatus();
		
		movieStatus.id = id;
		movieStatus.status = status;
		
		return movieStatus;
	}
}
