package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
//	@Query("select rs.seatNm from SeatReservation rs " + "left join rs.book bo " + "left join bo.theater th "
//			+ "where th.id = :theaterId")
//	public List<String> getReserveList(@Param("theaterId") String theaterId);

}