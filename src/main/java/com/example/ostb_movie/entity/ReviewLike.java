package com.example.ostb_movie.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.constant.LikeStatus;

import groovy.transform.ToString;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review_like")
@Getter
@Setter
@ToString
public class ReviewLike {
	@Id
	@Column(name = "review_like_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int reviewLike;
	
	@Enumerated(EnumType.STRING)
	private LikeStatus likeStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Review review;
}
