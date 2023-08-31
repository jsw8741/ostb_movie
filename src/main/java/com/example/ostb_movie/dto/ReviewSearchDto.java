package com.example.ostb_movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchDto {
	private String searchDateType;
	private String searchBy;
	private String searchQuery = "";
}
