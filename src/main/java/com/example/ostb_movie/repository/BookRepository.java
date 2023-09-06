package com.example.ostb_movie.repository;

import org.springframework.data.jpa.repository.*;

import com.example.ostb_movie.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}