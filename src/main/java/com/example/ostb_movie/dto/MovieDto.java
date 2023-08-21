package com.example.ostb_movie.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class MovieDto {
	private String imgUrl;
	
	private String movieTitle;
	
	private String description;
	
	private boolean adult;
	
	private String runTime;
	
	private Long originId;
}
