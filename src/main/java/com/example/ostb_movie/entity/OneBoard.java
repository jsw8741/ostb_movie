package com.example.ostb_movie.entity;



import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.constant.RoomStatus;
import com.example.ostb_movie.dto.OneBoardFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //엔티티 클래스로 정의
@Table(name="one_board") //테이블 이름 지정
@Getter
@Setter
@ToString
public class OneBoard extends BaseEntity {
	

	@Id
	@Column(name="room_id") //테이블로 생설될때 컬럼이름을 지정해준다
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 생성
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	@OnDelete(action= OnDeleteAction.CASCADE)
	private Member member;
	
	@Enumerated(EnumType.STRING)
	private RoomStatus roomStatus;
	
	private String sessionId;
	
	
	// 1:1 entity 가져오기
	public void getOne(OneBoardFormDto oneBoardFormDto) {
		this.sessionId = oneBoardFormDto.getSessionId();
	}
	
	//1:1 수정
	public void updateOne(OneBoardFormDto oneBoardFormDto) {
		roomStatus = RoomStatus.CLOSE;
	}
	
	
	
}
