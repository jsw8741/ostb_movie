package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.constant.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	private LocalDateTime paymentDate; // 결제일

	private String paymentMethode; // 결제방법

	@Enumerated(EnumType.STRING)
	PaymentStatus paymentStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id") // 결제랑 연결된 회원 정보
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Book book;

	public void cancelPayment() {
		this.paymentStatus = PaymentStatus.CANCEL;
		this.book.getSeat().setSeatNm(null);
	}
}