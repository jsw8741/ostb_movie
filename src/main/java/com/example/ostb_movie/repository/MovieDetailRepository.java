package com.example.ostb_movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.Movie;

public interface MovieDetailRepository extends JpaRepository<Movie, Long> {

}
