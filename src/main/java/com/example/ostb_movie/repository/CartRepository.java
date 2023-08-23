package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.dto.ItemSearchDto;
import com.example.ostb_movie.entity.Cart;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Order;

public interface CartRepository extends JpaRepository<Cart,Long>{
	@Modifying
	@Query(value = "delete from Cart where item_id = :itemId", nativeQuery = true)
	void deleteByitemId(@Param("itemId") long itemId);
	

	@Query(value = "select * from Cart where email = :email", nativeQuery = true)
	List<Cart> getCartItem(@Param("email") String email);
}
