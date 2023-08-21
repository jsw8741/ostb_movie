package com.example.ostb_movie.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.dto.MovieDto;
import com.example.ostb_movie.entity.MovieDetail;
import com.example.ostb_movie.entity.MovieStatus;
import com.example.ostb_movie.repository.MovieDetailRepository;
import com.example.ostb_movie.repository.MovieStatusRepository;
import com.nimbusds.jose.shaded.gson.*;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class ApiService {

    private final MovieDetailRepository movieDetailRepository;
    private final MovieStatusRepository movieStatusRepository;

    // 인기 순위
    public void getPopular() {
    	String popularUrl = "https://api.themoviedb.org/3/movie/popular?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR";
    	MovieStatus popularStatus = movieStatusRepository.getStatus("인기순위");
    	
    	try {
    		
			URL url = new URL(popularUrl);
			BufferedReader bf;
			bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String result = bf.readLine();
			
			JsonArray list = null;

	        JsonParser jsonParser = new JsonParser();
	        JsonObject jsonObject = (JsonObject) jsonParser.parse(result);
	        list = (JsonArray) jsonObject.get("results");
			
	        for (int k = 0; k < list.size(); k++) {
	        	JsonObject contents = (JsonObject) list.get(k);
	        	String originId = contents.get("id").toString();
	        	
	        	String ImgUrl = "https://image.tmdb.org/t/p/w200";
	            String match = "[\"]";
	            
	            String runTime = getRunTime(originId);
	            
	            MovieDto movieDto = MovieDto.builder()
	            		.originId(contents.get("id").getAsLong())
	                    .description(contents.get("overview").toString())
	                    .movieTitle(contents.get("title").toString())
	                    .imgUrl(ImgUrl + contents.get("poster_path").toString().replaceAll(match, ""))
	                    .adult(contents.get("title").getAsBoolean())
	                    .runTime(runTime)
	                    .build();
	            
	            MovieDetail movieDetail = MovieDetail.createMovie(movieDto, popularStatus);
	            movieDetailRepository.save(movieDetail);
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // 개봉 예정
    public void getUpComing() {
    	String upComingUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1&region=US";
    	
    	
    	try {
    		
			URL url = new URL(upComingUrl);
			BufferedReader bf;
			bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String result = bf.readLine();
			
			JsonArray list = null;

	        JsonParser jsonParser = new JsonParser();
	        JsonObject jsonObject = (JsonObject) jsonParser.parse(result);
	        list = (JsonArray) jsonObject.get("results");
			
	        for (int k = 0; k < list.size(); k++) {
	        	JsonObject contents = (JsonObject) list.get(k);
	        	String originId = contents.get("id").toString();
	        	
	        	String ImgUrl = "https://image.tmdb.org/t/p/w200";
	            String match = "[\"]";
	            
	            String runTime = getRunTime(originId);
	            
	            MovieDto movieDto = MovieDto.builder()
	            		.originId(contents.get("id").getAsLong())
	                    .description(contents.get("overview").toString())
	                    .movieTitle(contents.get("title").toString())
	                    .imgUrl(ImgUrl + contents.get("poster_path").toString().replaceAll(match, ""))
	                    .adult(contents.get("title").getAsBoolean())
	                    .runTime(runTime)
	                    .build();
	            
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // 상영중
    public void getNowPlaying() {
    	String nowPlatingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1&region=KR";
    	
    	
    	try {
    		
			URL url = new URL(nowPlatingUrl);
			BufferedReader bf;
			bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String result = bf.readLine();
			
			JsonArray list = null;

	        JsonParser jsonParser = new JsonParser();
	        JsonObject jsonObject = (JsonObject) jsonParser.parse(result);
	        list = (JsonArray) jsonObject.get("results");
			
	        for (int k = 0; k < list.size(); k++) {
	        	JsonObject contents = (JsonObject) list.get(k);
	        	String originId = contents.get("id").toString();
	        	
	        	String ImgUrl = "https://image.tmdb.org/t/p/w200";
	            String match = "[\"]";
	            
	            String runTime = getRunTime(originId);
	            
	            MovieDto movieDto = MovieDto.builder()
	            		.originId(contents.get("id").getAsLong())
	                    .description(contents.get("overview").toString())
	                    .movieTitle(contents.get("title").toString())
	                    .imgUrl(ImgUrl + contents.get("poster_path").toString().replaceAll(match, ""))
	                    .adult(contents.get("title").getAsBoolean())
	                    .runTime(runTime)
	                    .build();
	            
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // 영화 상세
    public String getRunTime(String originId) {
    	String movieDetailUrl = "https://api.themoviedb.org/3/movie/" + originId + "?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1" ;
        String runTime = null;
    	
        try {
			URL url = new URL(movieDetailUrl);
			BufferedReader bf;
			bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String resultDetail = bf.readLine();
			
			JsonParser jsonParserDetail = new JsonParser();
	        JsonObject jsonObjectDetail = (JsonObject) jsonParserDetail.parse(resultDetail);
	        runTime = jsonObjectDetail.get("runtime").toString();
	        
			return runTime;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
    }


}