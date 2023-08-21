package com.example.ostb_movie.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.dto.LostFormDto;
import com.example.ostb_movie.entity.Lost;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.repository.LostRepository;
import com.example.ostb_movie.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LostService {
	private final LostRepository lostRepository;
	private final MemberRepository memberRepository;

	// lost 테이블에 lostitem등록(insert)
	public Long saveLost(LostFormDto lostFormDto, String email) {
		Member member = memberRepository.findByEmail(email);
		Lost lost = lostFormDto.createLost(member);
		lostRepository.save(lost);
		return lost.getId();
	}

	// lost 가져오기
	@Transactional(readOnly = true)
	public Page<Lost> getMainLostDtl(Pageable pageable) {
		Page<Lost> lostPage = lostRepository.findAllByOrderByIdDesc(pageable);
		Long totalCount = lostRepository.count();
		return lostPage;
	}

	// lost 수정하기
	public Long updateLost(LostFormDto lostFormDto) {
		Lost lost = lostRepository.findById(lostFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		lost.updateLost(lostFormDto);
		return lost.getId();
	}

	// 본인확인
	@Transactional(readOnly = true)
	public boolean validateFaq(Long lostId, String email) {
		Member curMember = memberRepository.findByEmail(email);
		Lost lost = lostRepository.findById(lostId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = lost.getMember();
		if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		return true;
	}

	// lost 삭제
	public void deleteLost(Long lostId) {
		Lost lost = lostRepository.findById(lostId).orElseThrow(EntityNotFoundException::new);
		lostRepository.delete(lost);
	}

}
