package com.example.ostb_movie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.ItemFormDto;
import com.example.ostb_movie.dto.ItemImgDto;
import com.example.ostb_movie.dto.ItemSearchDto;
import com.example.ostb_movie.dto.MemberFormDto;
import com.example.ostb_movie.dto.MemberSearchDto;
import com.example.ostb_movie.dto.MypageFormDto;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Itemimg;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.repository.MemberRepository;
import com.example.ostb_movie.repository.MemberRepositoryCustom;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

	private final MemberRepository memberRepository;
	private final MemberImgService memberImgService;

	// 회원 등록
	public Member saveMember(Member member) {
		validateDuplicateMember(member); // 이메일 중복체크
		Member saveMember = memberRepository.save(member); // insert
		return saveMember; // 회원가입된 데이터를 칮는다.
	}

	// 이메일 중복 체크
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());

		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	// 이름 + 전화번호로 이메일 찾기
	public Member findEmail(String name, String phone) {
		Member member = memberRepository.getMemberEmail(name, phone);

		if (member == null) {
			throw new IllegalStateException("가입된 정보가 없습니다.");
		}

		return member;
	}

	// 이메일로 회원 정보 찾기
	public Member findMember(String email) {
		Member member = memberRepository.findByEmail(email);
		
		if(member == null) {
			throw new IllegalStateException("가입된 정보가 없습니다.");
		}
		
		return member;
	}

	// 비밀번호 변경
	public Member updatePassword(Member member) {
		memberRepository.save(member);
		return member;
	}
	public Member MemberId(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

		return member;

	}

	// 관리자 리스트
	public List<Member> getAdminList(){
		List<Member> adminList = memberRepository.getMasterList();
		
		return adminList;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email);
		if (member == null) {
			throw new UsernameNotFoundException(email);
		}

		return new PrincipalDetails(member);
	}

	public Long saveMember(MypageFormDto mypageFormDto, MultipartFile memberImgFile) throws Exception {
		Member member = mypageFormDto.createMypage();
		memberRepository.save(member);

		return member.getId();
	}

	// 1. 지금 접속한 멤버 찾기
	// 2. 찾은 멤버로 업데이트 메소드 실행
	public Long updateMember(MypageFormDto mypageFormDto, MultipartFile memberImgFile) throws Exception {
		
		Member member = memberRepository.findById(mypageFormDto.getId())
										.orElseThrow(EntityNotFoundException::new);
		member.updateMember(mypageFormDto);
		
		Long memberId = mypageFormDto.getId();
		memberImgService.UpdateMemberImg(mypageFormDto, memberId, memberImgFile);
		
		return member.getId();
	}

	public void updateAdminMember(@Valid MemberFormDto member) throws Exception {
		
		Member findmember = memberRepository.findByEmail(member.getEmail());
		
		findmember.updateAdminMember(member);
		
		memberRepository.save(findmember);
	}

	public Page<Member> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable) {
		Page<Member> memberPage = memberRepository.getAdminMemberPage(memberSearchDto, pageable);
		return memberPage;
	}
	
	// 닉네임만 업데이트
	public Member nickNameUpdate(MypageFormDto mypageFormDto, Long memberId) {		
		Member member = memberRepository.findById(mypageFormDto.getId())
				.orElseThrow(EntityNotFoundException::new);
		
		member = member.nickNameUpdate(mypageFormDto.getNickname());
		
		memberRepository.save(member);
		return member;
		}
	}
