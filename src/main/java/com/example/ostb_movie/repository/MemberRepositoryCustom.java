package com.example.ostb_movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ostb_movie.dto.MemberSearchDto;
import com.example.ostb_movie.entity.Member;

public interface MemberRepositoryCustom {
	Page<Member> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable);
}
