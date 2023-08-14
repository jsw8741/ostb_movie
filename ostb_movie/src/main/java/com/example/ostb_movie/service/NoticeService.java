package com.example.ostb_movie.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.dto.NoticeFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Notice;
import com.example.ostb_movie.repository.MemberRepository;
import com.example.ostb_movie.repository.NoticeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
	private final NoticeRepository noticeRepository;
	private final MemberRepository memberRepository;
	
	//notice 테이블에 notice등록(insert)
		public Long saveNotice(NoticeFormDto noticeFormDto, String email) {
			Member member = memberRepository.findByEmail(email);
			Notice notice = noticeFormDto.createNotice(member);
			noticeRepository.save(notice);
			
			/* 자동 인서트
			  Notice no1 = new Notice();
			  no1.setNoticeContent("asd");
			  no1.setNoticeTitle("dkdk");
			  no1.setMember(member);
			  noticeRepository.save(no1);
			 */
			
			return notice.getId();
		}
		
		
		//notice 가져오기
		@Transactional(readOnly = true)
		public Page<Notice> getMainNoticeDtl(Pageable pageable) {
			Page<Notice> noticePage = noticeRepository.findAllByOrderByIdDesc(pageable);
			Long totalCount = noticeRepository.count();
			return noticePage;
		}
		
		//notice 수정하기
		public Long updateNotice(NoticeFormDto noticeFormDto) {
			Notice notice = noticeRepository.findById(noticeFormDto.getId()).orElseThrow(EntityNotFoundException::new);
			notice.updateNotice(noticeFormDto);
			return notice.getId();
		}
		
		//본인확인
		@Transactional(readOnly = true)
		public boolean validateFaq(Long noticeId, String email) {
			Member curMember = memberRepository.findByEmail(email);
			Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
			
			Member savedMember = notice.getMember();
			
			if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
				return false;
			}
			
			return true;
		}
		
		//notice 삭제
		public void deleteNotice(Long noticeId) {
			Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
			noticeRepository.delete(notice);
		}
}
