package com.example.ostb_movie.dto;

import com.example.ostb_movie.constant.Categori;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ItemSearchDto {

	private String searchDateType;
	private Categori categori;
	private String searchBy;
	private String searchQuery = "";
}
