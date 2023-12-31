package com.example.ostb_movie.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.dto.MovieDto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie")
@Getter
@Setter
@ToString
public class Movie extends BaseEntity{
	@Id
	@Column(name="movie_id")
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
	
    private String genres;
    
    private String tagline;
    
    private String voteAverage;
    
    private String releaseDate;
    
    private String trailerUrl;
	
	public static Movie createMovie(Long id, MovieDto movieDto, MovieStatus status) {
		Movie movie = new Movie();
		
		movie.setId(id);
		movie.setImgUrl(movieDto.getImgUrl());
		movie.setMovieTitle(movieDto.getMovieTitle());
		movie.setDescription(movieDto.getDescription());
		movie.setAdult(movieDto.isAdult());
		movie.setRunTime(movieDto.getRunTime());
		movie.setOriginId(movieDto.getOriginId());
		movie.setStatus(status);
		movie.setGenres(movieDto.getGenres());
		movie.setTagline(movieDto.getTagline());
		movie.setVoteAverage(movieDto.getVoteAverage());
		movie.setReleaseDate(movieDto.getReleaseDate());
		movie.setTrailerUrl(movieDto.getTrailerUrl());
		
		
		return movie;
	}
}
