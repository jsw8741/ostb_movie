package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	private LocalDateTime paymentDate; // 결제일

	private String paymentType; // 결제타입

	private int discount; // 할인가격

	private int amount; // 총결제금액

	@ManyToOne
	@JoinColumn(name = "member_id") // 결제랑 연결된 회원 정보
	private Member member;
}
