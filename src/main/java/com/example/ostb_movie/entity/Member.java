package com.example.ostb_movie.entity;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ostb_movie.constant.Role;
import com.example.ostb_movie.dto.MemberFormDto;
import com.example.ostb_movie.dto.MypageFormDto;

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
	
	private Long point;

	private Long totalPay;
	
	private String memberImg;
	
	private String imgName;
	
	@Enumerated(EnumType.STRING)
	private Role role;	
	
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
		member.setMemberImg("/images/profile.png");
		member.setPoint(memberFormDto.getPoint());
		member.setTotalPay(memberFormDto.getTotalPay());
		member.setRole(Role.ROLE_USER);
		
		if(memberFormDto.getNickname().isEmpty()) {
			member.setNickname(memberFormDto.getName());
		}else {
			member.setNickname(memberFormDto.getNickname());
		}
		
		return member;
	}
	
	public void updateMember(MypageFormDto mypageFormDto) {
		this.nickname = mypageFormDto.getNickname();
		this.memberImg = mypageFormDto.getImgUrl();
	}
	
	public void updateMemberImg(String imgName,String memberImg) {
		this.imgName = imgName;
		this.memberImg = memberImg;
	}
	
	public static Member createMaster(PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode("12345678");
		
		Member member = new Member();
		
		member.setName("master");
		member.setEmail("master@naver.com");
		member.setPassword(password);
		member.setPhone("010-9999-9999");
		member.setRole(Role.ROLE_MASTER);
		
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
    public Member(String email, String password, String name, Role role, String provider, String providerId, Long point, Long totalPay) {
    	this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.point = point;
        this.totalPay = totalPay;
        
    }

}