package com.example.ostb_movie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ostb_movie.dto.CartDto;
import com.example.ostb_movie.dto.ItemSearchDto;
import com.example.ostb_movie.dto.OrderDto;
import com.example.ostb_movie.entity.Cart;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Order;
import com.example.ostb_movie.repository.CartRepository;
import com.example.ostb_movie.repository.ItemImgRepository;
import com.example.ostb_movie.repository.ItemRepository;
import com.example.ostb_movie.repository.MemberRepository;
import com.example.ostb_movie.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final ItemImgRepository itemImgRepository;

	public Cart saveCart(Cart cart) {
		Cart saveCart;
		saveCart = cartRepository.save(cart);

		return saveCart;
	}

	public Item setItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

		return item;
	}

	public Long Cart(CartDto cartDto, String email) {
		List<Cart> cartItemList = new ArrayList<>();
		Cart cartItem = Cart.createCart(cartDto, email);
		cartItemList.add(cartItem);

		cartRepository.save(cartItem);

		return cartItem.getId();

	}

	public void deletcart(Long cartid) {
		System.out.println("asdasd");
		cartRepository.deleteByitemId(cartid);

	}
	public List<Cart> getCartItem(String email) {
		List<Cart> CartList= cartRepository.getCartItem(email);
		return CartList;
	}
	 public Cart getCartItemById(Long itemId) {
	        return cartRepository.findById(itemId).orElse(null); // itemId에 해당하는 카트 항목 조회
	    }
}
