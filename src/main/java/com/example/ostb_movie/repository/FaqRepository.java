package com.example.ostb_movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ostb_movie.entity.Faq;


public interface FaqRepository extends JpaRepository<Faq, Long> {
	Page<Faq> findAllByOrderByIdDesc(Pageable pageable);
	
	Page<Faq> findByFaqRoleOrderByIdDesc(String FaqRole, Pageable pageable);
	
	@Query(value = "select * from faq where faq_role = '회원' order by faq_id desc", nativeQuery = true)
	Page<Faq> getFaqMember(Pageable pageable);
	
	
	@Query(value = "select count(*) from faq where faq_role = '회원'", nativeQuery = true)
	Long getFaqMemberTotal();
	
	@Query(value = "select * from faq where faq_role = '포인트' order by faq_id desc", nativeQuery = true)
	Page<Faq> getFaqPoint(Pageable pageable);
	
	@Query(value = "select count(*) from faq where faq_role = '포인트'", nativeQuery = true)
	Long getFaqPointTotal();
	
	@Query(value = "select * from faq where faq_role = '혜택' order by faq_id desc", nativeQuery = true)
	Page<Faq> getFaqBenefit(Pageable pageable);
	
	@Query(value = "select count(*) from faq where faq_role = '혜택'", nativeQuery = true)
	Long getFaqBenefitTotal();
	
	@Query(value = "select * from faq where faq_role = '친구' order by faq_id desc", nativeQuery = true)
	Page<Faq> getFaqFrend(Pageable pageable);
	
	@Query(value = "select count(*) from faq where faq_role = '친구'", nativeQuery = true)
	Long getFaqFrendTotal();
	
	
	
}
