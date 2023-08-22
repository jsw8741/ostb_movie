package com.example.ostb_movie.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ostb_movie.constant.OrderStatus;
import com.example.ostb_movie.dto.OrderDto;

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
@Table(name = "Orders")
@Getter
@Setter
@ToString
public class Order {

	@Id
	@Column(name = "Order_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private LocalDateTime orderDate; // 주문일

	private String Email;

	@OnDelete(action= OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item itemId;

	private int count;
	
	private String tprice;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // 주문상태

	public static Order createorder(OrderDto orderDto, String email) {
		Order order = new Order();
		order.setEmail(email);
		order.setItemId(orderDto.getItemId());
		order.setTprice(orderDto.getTotalprice());
		order.setOrderStatus(OrderStatus.ORDER);
		order.setCount(orderDto.getCount());

		return order;
	}

}