package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.constant.Categori;
import com.example.ostb_movie.entity.Itemimg;

public interface ItemImgRepository extends JpaRepository<Itemimg, Long> {

	Itemimg findByItemIdOrderByIdAsc(Long itemId);

	@Query("SELECT i FROM Itemimg i JOIN i.item item WHERE item.categori =:categori")
	List<Itemimg> findByCategori(@Param("categori")Categori categori);

}