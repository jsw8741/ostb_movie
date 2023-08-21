package com.example.ostb_movie.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.dto.FaqFormDto;
import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.repository.FaqRepository;
import com.example.ostb_movie.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {
	private final FaqRepository faqRepository;
	private final MemberRepository memberRepository;
	
	
	//faq 테이블에 faq등록(insert)
		public Long saveFaq(FaqFormDto faqFormDto, String email) {
			Member member = memberRepository.findByEmail(email);
			Faq faq = faqFormDto.createFaq(member);
			faqRepository.save(faq);
			return faq.getId();
		}
		
		
	//faq 가져오기
		@Transactional(readOnly = true)
		public Page<Faq> getMainFaqDtl(Pageable pageable) {
			Page<Faq> faqPage = faqRepository.findAllByOrderByIdDesc(pageable);
			Long totalCount = faqRepository.count();
			return faqPage;
		}
		
		
		
	//faq 수정하기
		public Long updateFaq(FaqFormDto faqFormDto) {
			Faq faq = faqRepository.findById(faqFormDto.getId()).orElseThrow(EntityNotFoundException::new);
			faq.updateFaq(faqFormDto);
			return faq.getId();
		}
		
	//본인확인
		@Transactional(readOnly = true)
		public boolean validateFaq(Long faqId, String email) {
			Member curMember = memberRepository.findByEmail(email);
			Faq faq = faqRepository.findById(faqId).orElseThrow(EntityNotFoundException::new);
			Member savedMember = faq.getMember();
			if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
				return false;
			}
			return true;
		}
		
	//faq 삭제
		public void deleteFaq(Long faqId) {
			Faq faq = faqRepository.findById(faqId).orElseThrow(EntityNotFoundException::new);
			faqRepository.delete(faq);
		}
		
	//faq 회원
		@Transactional(readOnly = true)
		public Page<Faq> getFaqMember(Pageable pageable) {
			return faqRepository.getFaqMember(pageable);
		}
		
	//faq 포인트
		@Transactional(readOnly = true)
		public Page<Faq> getFaqPoint(Pageable pageable) {
			return faqRepository.getFaqPoint(pageable);
		}
		
	//faq 혜택
		@Transactional(readOnly = true)
		public Page<Faq> getFaqBenefit(Pageable pageable) {
			return faqRepository.getFaqBenefit(pageable);
		}
		
	//faq 친구
		@Transactional(readOnly = true)
		public Page<Faq> getFaqFrend(Pageable pageable) {
			return faqRepository.getFaqFrend(pageable);
		}
		
}