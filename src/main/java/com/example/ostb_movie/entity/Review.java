package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.dto.ReviewDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
public class Review {
	@Id
	@Column(name = "review_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String content;
	
	private int rvLike;
	
	private LocalDateTime reviewDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id")
	private Movie movie;

	
	public void updateReview(ReviewDto reviewDto) {
		this.content = reviewDto.getContent();
		this.rvLike = reviewDto.getRvLike();
		this.reviewDate = reviewDto.getReviewDate();
	}
}
