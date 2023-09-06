package com.example.ostb_movie.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class}) 
@MappedSuperclass //부모 클래스를 상속받는 자식 클래스한테 매핑정보를 제공하기 위해
@Setter
@Getter
public class BaseTimeEntity {
	private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");
    
	@CreatedDate // 엔티티가 생성되서 저장될때 시간을 자동으로 저장
	@Column(updatable = false) // 컬럼의 값을 수정하지 못하게 막음
    private String regTime = ZonedDateTime.now(SEOUL_ZONE).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

	@LastModifiedDate //수정될때 시간을 자동으로 저장한다
    private String updateTime = ZonedDateTime.now(SEOUL_ZONE).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    
    
//	@CreatedDate // 엔티티가 생성되서 저장될때 시간을 자동으로 저장
//	@Column(updatable = false) // 컬럼의 값을 수정하지 못하게 막음
//	private String regTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); //등록날짜
//	
//	
//	@LastModifiedDate //수정될때 시간을 자동으로 저장한다
//	private String updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); //수정날짜
	
}
