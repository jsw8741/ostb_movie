package com.example.ostb_movie.service;

import java.io.BufferedReader;

import java.util.List;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.dto.MovieDto;
import com.example.ostb_movie.entity.Movie;
import com.example.ostb_movie.entity.MovieStatus;
import com.example.ostb_movie.repository.MovieRepository;
import com.example.ostb_movie.repository.MovieStatusRepository;
import com.nimbusds.jose.shaded.gson.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieStatusRepository movieStatusRepository;
    
    // 영화 aip 주기적 업데이트
    public void updateMovie() {
    	movieRepository.deleteAll();
    	getPopular();
    	getUpComing();
    	getNowPlaying();
    }

    // 인기 순위
    public void getPopular() {
    	String popularUrl = "https://api.themoviedb.org/3/movie/popular?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR";
    	MovieStatus popularStatus = movieStatusRepository.getStatus("인기순위");
    	getApi(popularUrl, 1, popularStatus);
    }
    
    // 개봉 예정
    public void getUpComing() {
    	String upComingUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1&region=US";
    	MovieStatus upComingStatus = movieStatusRepository.getStatus("개봉예정");
    	getApi(upComingUrl, 100, upComingStatus);
    }
    
    // 상영중
    public void getNowPlaying() {
    	String nowPlatingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1&region=KR";
    	MovieStatus nowPlayingStatus = movieStatusRepository.getStatus("상영중");
    	getApi(nowPlatingUrl, 200, nowPlayingStatus);
    }
    
    // Api로 영화 등록
    public void getApi(String getUrl, int baseId, MovieStatus movieStatus) {
    	try {
    		
			URL url = new URL(getUrl);
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
	            
	            String runTime = getRunTime(originId, "runTime");
	            String genres = getRunTime(originId, "genres");
	            
	            MovieDto movieDto = MovieDto.builder()
	            		.originId(contents.get("id").getAsLong())
	                    .description(contents.get("overview").toString())
	                    .movieTitle(contents.get("title").toString())
	                    .imgUrl(ImgUrl + contents.get("poster_path").toString().replaceAll(match, ""))
	                    .adult(contents.get("title").getAsBoolean())
	                    .runTime(runTime)
	                    .genres(genres)
	                    .build();
	            
	            Long id = (long) (k + baseId);
	            
	            Movie movieDetail = Movie.createMovie(id, movieDto, movieStatus);
	            movieRepository.save(movieDetail);
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    // 영화 상세에서 상영시간 가져오기
    public String getRunTime(String originId, String data) {
    	String movieDetailUrl = "https://api.themoviedb.org/3/movie/" + originId + "?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1" ;
    	
        try {
			URL url = new URL(movieDetailUrl);
			BufferedReader bf;
			bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String resultDetail = bf.readLine();
			
			JsonParser jsonParserDetail = new JsonParser();
	        JsonObject jsonObjectDetail = (JsonObject) jsonParserDetail.parse(resultDetail);
	        
	        if(data.equals("runTime")) {
	        	String runTime = jsonObjectDetail.get("runtime").toString();
	        	return runTime;	        	
	        }else if(data.equals("genres")) {
	        	String genres = null; 
	        	
	        	JsonArray list = (JsonArray) jsonObjectDetail.get("genres");
	        	
	        	for(int i=0; i<list.size();i++) {
	        		JsonObject contents = (JsonObject) list.get(i);
	        		if(i == 0) {
	        			String originalData = contents.get("name").toString();
	        			genres = originalData.replace("\"", "");
	        			
	        		}else {
	        			String originalData = contents.get("name").toString();
	        			genres += " " + originalData.replace("\"", "");   			
	        		}
	        		
	        	}
		      
		        return genres;
	        	
	        }else {
	        	return null;
	        }
	        
	        
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
    }
    
    // 상영 시간(상세)
    public String getRunTime(JsonObject jsonObjectDetail) {
    	
    	return null;
    }
    
    // 영화 장르(상세)
    
    // 영화 전체 리스트 가져오기
    public List<Movie> getMovieAll(){
    	List<Movie> movieList = movieRepository.findAll();
    	
    	return movieList;
    }
    
    // 영화 차트 가져오기(인기순위)
    public List<Movie> getMoviePopular(){
    	List<Movie> moviePopularList = movieRepository.getPopularList((long) 1);
    	
    	return moviePopularList;
    }

}