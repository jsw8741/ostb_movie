package com.example.ostb_movie.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.dto.BookDto;
import com.example.ostb_movie.service.BookService;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {
	@Id
	@Column(name = "book_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String bookNo; // 예매번호

	private int totalPrice; // 총금액

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Seat seat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Payment payment;

	public static Book createBook(BookDto bookDto) {
		Book book = new Book();
		book.setBookNo(bookDto.getBookNo());
		book.setTotalPrice(bookDto.getTotalPrice());
		book.setSeat(bookDto.getSeat());
		book.setPayment(bookDto.getPayment());

		return book;
	}

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "theater_id")
	 * 
	 * @OnDelete(action = OnDeleteAction.CASCADE) private Theater theater;
	 */

	/*
	 * @OneToMany(mappedBy = "book")
	 * 
	 * @OnDelete(action = OnDeleteAction.CASCADE) private List<SeatReservation>
	 * seatReservationList = new ArrayList<SeatReservation>();
	 */
}