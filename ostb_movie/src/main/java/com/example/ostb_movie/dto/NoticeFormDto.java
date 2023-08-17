package com.example.ostb_movie.dto;

import org.modelmapper.ModelMapper;

import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Notice;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeFormDto {
	
	private Long id;
	
	@NotBlank(message = "FAQ 제목을 입력해주세요.")
	private String noticeTitle;
	
	@NotBlank(message = "FAQ 내용을 입력해주세요.")
	private String noticeContent;
	
	private long memberId;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	
	//dto -> entity로 바꿔줌
	public Notice createNotice(Member member) {
		this.memberId = member.getId();
		return modelMapper.map(this, Notice.class);
	}
	
	//entity를 dto로 바꿔줌
	public static NoticeFormDto of(Notice notice) {
		return modelMapper.map(notice, NoticeFormDto.class);
	}
	
	public Notice insertNotice() {
		return modelMapper.map(this, Notice.class);
	}
}
