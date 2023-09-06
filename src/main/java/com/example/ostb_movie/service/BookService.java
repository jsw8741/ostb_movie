package com.example.ostb_movie.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.entity.*;
import com.example.ostb_movie.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final SeatRepository seatRepository;

	// 티켓 예매 처리
	public Book submitTicketing(Map<String, Object> paramMap) {
		Theater theater = new Theater();
		theater.setId(Long.parseLong(paramMap.get("theaterId").toString()));

		// 좌석 객체 생성
		String seatListStr = (String) paramMap.get("seatList");
		Seat seat = new Seat();
		seat.setSeatNm(seatListStr);
		seat.setTheater(theater);

		// 좌석 등록
		saveSeat(seat);

		// 예매 객체 생성, 등록
		Book book = new Book();
		String bookNo = createBookNo(); // 예매번호 생성
		book.setBookNo(bookNo);
		book.setPrice(Integer.parseInt(paramMap.get("totalPrice").toString()));
		book.setSeat(seat);

		return saveBook(book);
	}

	// 좌석 등록
	public void saveSeat(Seat seat) {
		seatRepository.save(seat);
	}

	// 예매 등록
	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	// 예매번호 생성
	public String createBookNo() {
		String uniqKey = "OSTB"; // 고유문자
		int randomNo = (int) (Math.random() * 9000) + 1000; // 4자리 랜덤숫자
		// 예매번호 형식: 20230830-OSTB3242
		return LocalDate.now().toString().replace("-", "") + "-" + uniqKey + String.format("%04d", randomNo);
	}

	// 예매된 좌석목록 가져오기
	public List<String> getSeatList(String theaterId) {
		return seatRepository.getSeatList(theaterId);
	}

}