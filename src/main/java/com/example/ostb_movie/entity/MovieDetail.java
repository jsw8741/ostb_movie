package com.example.ostb_movie.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.dto.MovieDto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_detail")
@Getter
@Setter
public class MovieDetail {
	@Id
	@Column(name="movie_detail_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String imgUrl;
	
	@Column(nullable = false)
    private String movieTitle;
	
	@Lob
    @Column(nullable = false, columnDefinition = "longtext")
    private String description;
	
	private boolean adult;
	
	@Column(nullable = false)
    private String runTime;
	
	@Column(nullable = false)
    private Long originId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_status_id")
	@OnDelete(action= OnDeleteAction.CASCADE)
	private MovieStatus status;
	
	public static MovieDetail createMovie(MovieDto movieDto, MovieStatus status) {
		MovieDetail movie = new MovieDetail();
		
		movie.setOriginId(movieDto.getOriginId());
		movie.setImgUrl(movieDto.getImgUrl());
		movie.setMovieTitle(movieDto.getMovieTitle());
		movie.setDescription(movieDto.getDescription());
		movie.setAdult(movieDto.isAdult());
		movie.setRunTime(movieDto.getRunTime());
		movie.setStatus(status);
		
		return movie;
	}
}
