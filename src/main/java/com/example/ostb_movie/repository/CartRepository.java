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
	@Query(value = "delete from cart where cart_id = :cartId", nativeQuery = true)
	void deleteByitemId(@Param("cartId") long cartId);
	

	@Query(value = "select * from cart where email = :email", nativeQuery = true)
	List<Cart> getCartItem(@Param("email") String email);
}
