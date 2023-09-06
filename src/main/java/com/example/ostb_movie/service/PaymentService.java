package com.example.ostb_movie.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.entity.Payment;
import com.example.ostb_movie.repository.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentRepository paymentRepository;

	// 예매완료 목록 조회
	@Transactional(readOnly = true)
	public List<Payment> getCompleteList(String email) {
		return paymentRepository.getCompleteList(email);
	}

	// 예매취소 목록 조회
	@Transactional(readOnly = true)
	public List<Payment> getCancelList(String email) {
		return paymentRepository.getCancelList(email);
	}

	public Payment submitPayment(Map<String, Object> paramMap) {
		return null;
	}

	// 결제 등록
	public Payment savePayment(Payment payment) {
		return paymentRepository.save(payment);
	}
	
	// 결제 취소
	public void cancelPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				                  .orElseThrow(EntityNotFoundException::new);
		
		payment.cancelPayment();
	}
}
