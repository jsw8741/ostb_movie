package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Cart;

public interface CartRepository extends JpaRepository<Cart,Long>{
	@Modifying
	@Query(value = "delete from cart where cart_id = :cartId", nativeQuery = true)
	void deleteByitemId(@Param("cartId") long cartId);
	

	@Query(value = "select c from Cart c where email = :email", nativeQuery = true)
	List<Cart> getCartItem(@Param("email") String email);
}
