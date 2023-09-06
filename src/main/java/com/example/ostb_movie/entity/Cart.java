package com.example.ostb_movie.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.constant.CartStatus;
import com.example.ostb_movie.dto.CartDto;

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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {
	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String Email;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item itemId;

	private int count;

	private String tprice;

	@Enumerated(EnumType.STRING)
	private CartStatus cartStatus; // 주문상태

	public static Cart createCart(CartDto cartDto, String email) {
		Cart cart = new Cart();
		cart.setEmail(email);
		cart.setItemId(cartDto.getItemId());
		cart.setTprice(cartDto.getTotalprice());
		cart.setCartStatus(CartStatus.IN);
		cart.setCount(cartDto.getCount());

		return cart;
	}

}
