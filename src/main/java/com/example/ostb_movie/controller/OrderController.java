package com.example.ostb_movie.controller;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.OrderDto;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@GetMapping(value = "/order")
	public String orderForm(Model model) {
		model.addAttribute("OrderDto", new OrderDto());
		return "order/orderForm"; // orderForm.html 페이지를 반환
	}

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
}