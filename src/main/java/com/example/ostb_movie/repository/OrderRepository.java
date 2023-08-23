package com.example.ostb_movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Order;


public interface OrderRepository extends JpaRepository<Order,Long>{
	@Modifying
	@Query(value = "delete from orders where item_id = :itemId", nativeQuery = true)
	void deleteByitemId(@Param("itemId") long itemId);
}
