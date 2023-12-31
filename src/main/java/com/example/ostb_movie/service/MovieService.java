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
    
    // 상영중
    public void getNowPlaying() {
    	String nowPlatingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1&region=KR";
    	MovieStatus nowPlayingStatus = movieStatusRepository.getStatus("상영중");
    	getApi(nowPlatingUrl, 100, nowPlayingStatus);
    }
    
    // 개봉 예정
    public void getUpComing() {
    	String upComingUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1&region=US";
    	MovieStatus upComingStatus = movieStatusRepository.getStatus("개봉예정");
    	getApi(upComingUrl, 200, upComingStatus);
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
	        	
	        	String ImgUrl = "https://image.tmdb.org/t/p/w500";
	            String match = "[\"]";
	            
	            String runTime = getRunTime(originId, "runTime");
	            String genres = getRunTime(originId, "genres");
	            String tagline = getRunTime(originId, "tagline");
	            String trailerUrl = getTrailerKey(originId);
	            
	            if(trailerUrl != null) {
	            	trailerUrl = "https://www.youtube.com/embed/" + trailerUrl;
	            }
	            
	            MovieDto movieDto = MovieDto.builder()
	            		.originId(contents.get("id").getAsLong())
	                    .description(contents.get("overview").toString().replace("\"", ""))
	                    .movieTitle(contents.get("title").toString().replace("\"", ""))
	                    .imgUrl(ImgUrl + contents.get("poster_path").toString().replaceAll(match, ""))
	                    .adult(contents.get("title").getAsBoolean())
	                    .runTime(runTime)
	                    .genres(genres)
	                    .tagline(tagline)
	                    .voteAverage(contents.get("vote_average").toString())
	                    .releaseDate(contents.get("release_date").toString().replace("\"", ""))
	                    .trailerUrl(trailerUrl)
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
    	String movieDetailUrl = "https://api.themoviedb.org/3/movie/" + originId + "?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR&page=1";
    	
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
	        			genres = contents.get("name").toString().replace("\"", "");
	        		}else {
	        			genres += " " + contents.get("name").toString().replace("\"", "");
	        		}
	        		
	        	}
		      
		        return genres;
	        	
	        }else if(data.equals("tagline")) {
	        	String tagline = jsonObjectDetail.get("tagline").toString().replace("\"", "");
	        	return tagline;
	        }else {
	        	return null;
	        }
	        
	        
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
    }
    
    // 영화 예고편 가져오기
    public String getTrailerKey(String originId) {
    	String movieTrailerUrl = "https://api.themoviedb.org/3/movie/" + originId + "/videos?api_key=d0f57e4e20e63bfcf331ff49a646c74c&language=ko-KR";
	    
    	String trailerKet = null;
    	
    	try {
    		URL url = new URL(movieTrailerUrl);
			BufferedReader bf;
			bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String resultTrailer = bf.readLine();
			
			JsonArray list = null;
			
			JsonParser jsonParserTrailer = new JsonParser();
	        JsonObject jsonObjectTrailer = (JsonObject) jsonParserTrailer.parse(resultTrailer);
	        list = (JsonArray) jsonObjectTrailer.get("results");
	        
	        if(list.size()>1) {
	        	JsonObject contents = (JsonObject) list.get(0);
	        	
	        	trailerKet = contents.get("key").toString().replace("\"", "");
	        	
	        	return trailerKet;
	        }else {
	        	return null;
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    // 영화 정보 가져오기(상세)
    public Movie getMovieDtl(Long originId) {
    	Movie movieDtl = movieRepository.getMovieDtl(originId);
    	
    	return movieDtl;
    }
    
    // 영화 전체 리스트 가져오기
    public List<Movie> getMovieAll(){
    	List<Movie> movieList = movieRepository.findAll();
    	
    	return movieList;
    }
    
    // 영화 차트 가져오기(인기순위)
    public List<Movie> getMoviePopular(){
    	List<Movie> moviePopularList = movieRepository.getMovieStatusList((long) 1);
    	
    	return moviePopularList;
    }
    
    // 영화 차트 가져오기(상영중)
    public List<Movie> getMovieNowPlaying(){
    	List<Movie> moviePopularList = movieRepository.getMovieStatusList((long) 2);
    	
    	return moviePopularList;
    }
    
    // 영화 차트 가져오기(개봉예정)
    public List<Movie> getMovieUpComming(){
    	List<Movie> moviePopularList = movieRepository.getMovieStatusList((long) 3);
    	
    	return moviePopularList;
    }

}