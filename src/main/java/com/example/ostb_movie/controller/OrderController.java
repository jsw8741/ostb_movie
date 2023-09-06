package com.example.ostb_movie.controller;

import java.awt.ItemSelectable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.CartDto;
import com.example.ostb_movie.dto.OrderDto;
import com.example.ostb_movie.entity.Cart;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.Order;
import com.example.ostb_movie.service.CartService;
import com.example.ostb_movie.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
	private final CartService cartService;

	@PostMapping(value = "/order")
	public String order(@Valid OrderDto orderDto, BindingResult bindingResult, Authentication authentication,
			@RequestParam("itemId") Long ItemId) {

		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			return "redirect:/";
		}

		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();
		String email = member.getEmail();

		Long itemId = ItemId;
		try {
			Item item = orderService.setItem(itemId);
			orderDto.setItemId(item);
			orderService.order(orderDto, email); // 주문하기 실행
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return "redirect:/";
		}

		return "redirect:/";
	}

	@PostMapping(value = "/order/cart")
	public String cartItem(@Valid CartDto cartDto, BindingResult bindingResult, Authentication authentication,
			@RequestParam("itemId") Long ItemId) {

		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			return "redirect:/";
		}

		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();
		String email = member.getEmail();
		Long itemId = ItemId;
		try {
			Item item = cartService.setItem(itemId);
			cartDto.setItemId(item);
			cartService.Cart(cartDto, email); // 주문하기 실행
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return "redirect:/";
		}
		return "redirect:/";
	}

	@GetMapping(value = "/order/myCart")
	public String myCart(Model model, Authentication authentication, @ModelAttribute("CartDto") CartDto cartDto,
			BindingResult bindingResult) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();
		String email = member.getEmail();
		List<Cart> carts = cartService.getCartItem(email);
		List<Cart> orderList = new ArrayList<>();
		
		model.addAttribute("carts", carts);
		model.addAttribute("orderList", orderList);
		model.addAttribute("member", member);
		return "order/myCart";
	}

	@PostMapping("/order/cartsign")
	public String addToOrder(@RequestParam(name = "selectedItems", required = false) List<Long> selectedItems,
			Authentication authentication, Model model) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();

		List<Cart> orderList = new ArrayList<>();
		if (selectedItems != null && !selectedItems.isEmpty()) {
			for (Long itemId : selectedItems) {
				Cart selectedCart = cartService.getCartItemById(itemId);
				orderList.add(selectedCart);
			}
			model.addAttribute("orderList", orderList);
		}

		return "order/myCart";
	}

	@GetMapping(value = "/order/orderList")
	public String OrderList(Model model, Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();
		String email = member.getEmail();

		List<Order> AllList = orderService.findAllOrders(email);
		List<Order> NoUsedList = orderService.NoUsedOrder(email);
		
		model.addAttribute("AllList", AllList);
		model.addAttribute("NoUsedList", NoUsedList);
		model.addAttribute("member", member);
		return "order/myOrderList";
	}
}