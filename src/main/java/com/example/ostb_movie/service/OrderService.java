package com.example.ostb_movie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.dto.OrderDto;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Order;
import com.example.ostb_movie.repository.ItemImgRepository;
import com.example.ostb_movie.repository.ItemRepository;
import com.example.ostb_movie.repository.MemberRepository;
import com.example.ostb_movie.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final ItemImgRepository itemImgRepository;

	public Order saveReservation(Order order) {
		Order  saveOrder;
		saveOrder = orderRepository.save(order);

		return saveOrder; // 회원가입된 데이터를 칮는다.
	}

	public Item setItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

		return item;
	}

	public Long order(OrderDto orderDto, String email) {
		List<Order> orderItemList = new ArrayList<>();
		Order orderItem = Order.createorder(orderDto, email);
		orderItemList.add(orderItem);

		orderRepository.save(orderItem);

		return orderItem.getId();

	}

	public void deletItems(Long itemId) {
		orderRepository.deleteByitemId(itemId);

	}
}
