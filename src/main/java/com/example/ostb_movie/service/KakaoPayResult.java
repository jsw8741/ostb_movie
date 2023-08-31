package com.example.ostb_movie.service;

import java.util.Map;

import com.example.ostb_movie.dto.KakaoPayReadyDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayResult {
	  private KakaoPayReadyDto dto;
	    private Map<String, Object> params;

	    public static KakaoPayResult create(KakaoPayReadyDto dto, Map<String, Object> params) {
	        KakaoPayResult result = new KakaoPayResult();
	        result.setDto(dto);
	        result.setParams(params);
	        return result;
	    }
	}