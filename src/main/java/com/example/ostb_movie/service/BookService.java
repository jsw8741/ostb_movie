package com.example.ostb_movie.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.entity.*;
import com.example.ostb_movie.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final SeatRepository seatRepository;
	// private final SeatRepository sRRepository;
	
	// 예매번호 만들기
	private static String createBookNo() {
		String uniqKey = "OSTB"; // 고유문자
		int randomNo = (int) (Math.random() * 9000) + 1000; // 4자리 랜덤숫자
		// 20230830-OSTB3242 이런식으로 생성
		String bookNo = LocalDate.now().toString().replace("-", "") + "-" + uniqKey + String.format("%04d", randomNo);
		return bookNo;
	}

	// 예약 저장
	public Long saveBook(Book book) {
		// 예매번호 생성
		String bookNo = createBookNo();
		book.setBookNo(bookNo);
		bookRepository.save(book);
		return book.getId();
	}

	// 좌석 예약 DB에 저장
	public void insertSeat(Seat listSR) {
		seatRepository.save(listSR);
	}

	/*
	 * //상영관 예약목록 조회 public List getReserveList(String theaterId) { return
	 * seatRepository.getReserveList(theaterId); }
	 */
}