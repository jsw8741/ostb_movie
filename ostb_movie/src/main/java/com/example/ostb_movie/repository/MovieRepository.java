package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	@Query("select m from Movie m where m.status.id = :movie_status_id")
	List<Movie> getPopularList(@Param("movie_status_id") Long movie_status_id);
	
}
