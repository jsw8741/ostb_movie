package com.example.ostb_movie.entity;

import com.example.ostb_movie.constant.Categori;
import com.example.ostb_movie.constant.ItemStatus;
import com.example.ostb_movie.dto.ItemFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 엔티티 클래스로 정의
@Table(name = "item") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Item {
	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 50)
	private String itemNm;

	@Column(nullable = false)
	private int price;

	@Lob
	@Column(nullable = false, columnDefinition = "longtext")
	private String itemDetail;

	@Enumerated(EnumType.STRING)
	private Categori categori;
	
	public void updateItem(ItemFormDto itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.itemDetail = itemFormDto.getItemDetail();
		this.categori = itemFormDto.getCategori();
	}

}
