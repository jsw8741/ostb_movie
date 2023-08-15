package com.example.ostb_movie.entity;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ostb_movie.constant.Role;
import com.example.ostb_movie.dto.MemberFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member{

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true) // 중복된 값이 들어올 수 없다
	private String email;

	private String name;
	
	private String password;

	private String phone;

	private String provider;

	private String providerId;

	private String nickname;
	
	private String birth;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	// MemberFormDto를 -> Member 엔티티 객체로 변환
//	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
//		// 패스워드 암호화
//		String password = passwordEncoder.encode(memberFormDto.getPassword());
//
//		Member member = new Member(memberFormDto.getName() , memberFormDto.getEmail() , memberFormDto.getAddress());
//		member.setPassword(password);
//		member.setRole(Role.ADMIN); //관리자로 가입할때
////		member.setRole(Role.USER); // 일반 사용자로 가입할때
//
//		return member;
//	}
	
//	public Member (String name , String email) {
//		this.name = name;
//		this.email = email;
//	}
	
	
	
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		Member member = new Member();
		String password;
		if(memberFormDto.getPassword().equals("SNS 로그인")) {
			password = "SNS 로그인";
			member.setProvider(memberFormDto.getProvider());
			member.setProviderId(memberFormDto.getProviderId());
		}else {
			password = passwordEncoder.encode(memberFormDto.getPassword());
		}
		
		
		
		member.setEmail(memberFormDto.getEmail());
		member.setPassword(password);
		member.setName(memberFormDto.getName());
		member.setPhone(memberFormDto.getPhone());
		member.setBirth(memberFormDto.getBirth());
		member.setRole(Role.USER);
		
		if(memberFormDto.getNickname().isEmpty()) {
			member.setNickname(memberFormDto.getName());
		}else {
			member.setNickname(memberFormDto.getNickname());
		}
		
		return member;
	}
	
	@Builder(builderClassName = "MemberDetailRegister", builderMethodName = "MemberDetailRegister")
    public Member(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public Member(String email, String password, String name, Role role, String provider, String providerId) {
    	this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}