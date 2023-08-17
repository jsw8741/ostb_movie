package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Faq;
import com.example.ostb_movie.entity.Member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqFormDto {
	
	private Long id;
	
	@NotBlank(message = "카테고리를 선택해주세요.")
	private String faqRole;
	
	@NotBlank(message = "FAQ 제목을 입력해주세요.")
	private String faqTitle;
	
	@NotBlank(message = "FAQ 내용을 입력해주세요.")
	private String faqContent;
	
	private long memberId;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//dto -> entity로 바꿔줌
	public Faq createFaq(Member member) {
		this.memberId = member.getId();
		return modelMapper.map(this, Faq.class);
	}
	
	//entity를 dto로 바꿔줌
	public static FaqFormDto of(Faq faq) {
		return modelMapper.map(faq, FaqFormDto.class);
	}
	
}
