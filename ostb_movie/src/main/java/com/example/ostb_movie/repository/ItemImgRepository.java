package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.Itemimg;

public interface ItemImgRepository extends JpaRepository<Itemimg, Long> {

	List<Itemimg> findByItemIdOrderByIdAsc(Long itemId);

}