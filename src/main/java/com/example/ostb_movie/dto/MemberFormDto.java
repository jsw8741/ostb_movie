package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.constant.Role;
import com.example.ostb_movie.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
public class MemberFormDto {

	@NotEmpty(message = "이메일은 필수입력 값입니다.")
	@Email(message = "이메일 형식으로 입력해주세요.")
	private String email;
	
	@NotEmpty(message = "비밀번호는 필수입력 값입니다.")
	private String password;
	
	@NotBlank(message = "이름은 필수입력 값입니다.")
	private String name;
	
	private String nickname;
	
	@NotEmpty(message = "전화번호는 필수입력 값입니다.")
	private String phone;
	
	private String provider;

	private String providerId;
	
	private String birth;
	
	private Long point;
	
	private Long totalPay;
	
	private Role role;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//entity -> dto
	public static MemberFormDto of(Member member) {
		return modelMapper.map(member, MemberFormDto.class);
	}
	
	
}