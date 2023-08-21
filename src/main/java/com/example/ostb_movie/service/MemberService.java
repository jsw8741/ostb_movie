package com.example.ostb_movie.service;

<<<<<<< HEAD
import java.util.List;

import org.springframework.security.core.userdetails.User;
=======
>>>>>>> 65f934d6c09fcadfd8486d8462ff49bd5817ac5b
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.ostb_movie.auth.PrincipalDetails;
<<<<<<< HEAD
import com.example.ostb_movie.dto.MypageFormDto;
=======
>>>>>>> 65f934d6c09fcadfd8486d8462ff49bd5817ac5b
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{
	
	private final MemberRepository memberRepository;
	private final MemberImgService memberImgService;
	
	// 회원 등록
	public Member saveMember(Member member) {
		validateDuplicateMember(member); // 이메일 중복체크
		Member saveMember = memberRepository.save(member); // insert
		return saveMember; //회원가입된 데이터를 칮는다.
	}
	
	// 이메일 중복 체크
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		
		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}
	
	// 이름 + 전화번호로 이메일 찾기
	public Member findEmail(String name, String phone) {
		Member member = memberRepository.getMemberEmail(name, phone);
		
		if(member == null) {
			throw new IllegalStateException("가입된 정보가 없습니다.");
		}
		
		return member;
	}
	
	// 이메일로 회원 정보 찾기
	public Member findMember(String email) {
		Member member = memberRepository.findByEmail(email);
		return member;
	}
	
	// 비밀번호 변경
	public Member updatePassword(Member member) {
		memberRepository.save(member);
		return member;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email);
		if(member == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new PrincipalDetails(member);
	}
	
	// 1. 지금 접속한 멤버 찾기
	// 2. 찾은 멤버로 업데이트 메소드 실행
	public Long updateMember(MypageFormDto mypageFormDto, MultipartFile memberImgFile) throws Exception {
		System.out.println(mypageFormDto.getId() + "asdasd");
		Member member = memberRepository.findById(mypageFormDto.getId())
										.orElseThrow(EntityNotFoundException::new);
		member.updateMember(mypageFormDto);
		return member.getId();
	}
	
}