package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	private LocalDateTime paymentDate; // 결제일

	private String paymentType; // 결제타입

	private int discount; // 할인

	private int totalPrice; // 총금액

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id") // 결제랑 연결된 회원 정보
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;
}
