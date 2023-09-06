package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.*;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	// 마이페이지 예매내역
	@Query("select p from Payment p where p.member.email = :email and p.paymentStatus = 'COMPLETE' order by p.paymentDate limit 3")
	List<Payment> getTicketList(@Param("email") String email);
	
	@Query("select p from Payment p where p.member.email = :email and p.paymentStatus = 'COMPLETE' order by p.paymentDate desc")
	List<Payment> getCompleteList(@Param("email") String email);

	@Query("select p from Payment p where p.member.email = :email and p.paymentStatus = 'CANCEL' order by p.paymentDate desc")
	List<Payment> getCancelList(@Param("email") String email);
}
