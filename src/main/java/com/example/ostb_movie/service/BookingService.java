package com.example.ostb_movie.service;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ostb_movie.entity.*;
import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {
	private final BookService bookService;
	private final TheaterService theaterService;
	// 티켓 예매정보 처리
	public void submitTicketing(Map<String, Object> paramMap) {
		try {
			// 예매정보 생성, 저장
			Book book = createBookFromRequest(paramMap);
			List<Seat> seat = createSeatReservationsFromRequest(paramMap, book);
			
			// 예매정보, 좌석예약정보 DB에 저장
			Long bookId = bookService.saveBook(book);
			book.setId(bookId);
			bookService.insertSeat(seat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 예매정보 생성
	private Book createBookFromRequest(Map<String, Object> paramMap) {
		Book book = new Book();
		Theater theater = new Theater();
		theater.setId(Long.parseLong(paramMap.get("theaterId").toString()));
		List<String> seatList = (List<String>) paramMap.get("seatList");
		// 예매번호 생성
		String bookNo = createBookNo();
		book.setBookNo(bookNo);
//		book.setTheater(theater);
		book.setTotalPrice(Integer.parseInt(paramMap.get("totalPrice").toString()));
		return book;
	}
	// 예매번호 만들기
	private static String createBookNo() {
		String uniqKey = "OSTB"; // 고유문자
		int randomNo = (int) (Math.random() * 9000) + 1000; // 4자리 랜덤숫자
		// 20230830-OSTB3242 이런식으로 생성
		String bookNo = LocalDate.now().toString().replace("-", "") + "-" + uniqKey + String.format("%04d", randomNo);
		return bookNo;
	}
	// 좌석예약정보 생성
	private List<Seat> createSeatReservationsFromRequest(Map<String, Object> paramMap, Book book) {
		List<Seat> seat = new ArrayList<>();
		List<String> seatList = (List<String>) paramMap.get("seatList");
		for (int i = 0; i < Integer.parseInt(paramMap.get("seatCnt").toString()); i++) {
			Seat sR = new Seat();
			sR.setPrice(10000); // 가격 설정
//			sR.setTheater(null); 여기서하기
			sR.setName(String.valueOf(seatList.get(i))); // 좌석 설정
			seat.add(sR);
		}
		return seat;
	}
}