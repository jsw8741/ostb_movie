package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.dto.ItemFormDto;
import com.example.ostb_movie.dto.ItemImgDto;
import com.example.ostb_movie.dto.MainItemDto;
import com.example.ostb_movie.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

	@Query(value = "select * from item where item_detail like %:itemDetail% order by price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

	@Query("select i from Item i where i.price >= :price")
	List<Item> findByPrice(@Param("price") int price);

	@Query("select i from Itemimg i where i.item.id = :itemId")
	ItemImgDto findByItemImg(@Param("itemId") Long itemId);

	@Modifying
	@Query(value = "delete from item where item_id = :itemId", nativeQuery = true)
	void deleteByitemIdByNative(@Param("itemId") long item);
	
}