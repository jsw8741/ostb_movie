package com.example.ostb_movie.controller;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ostb_movie.constant.OrderStatus;
import com.example.ostb_movie.dto.KakaoPayApproveDto;
import com.example.ostb_movie.dto.KakaoPayReadyDto;
import com.example.ostb_movie.dto.OrderDto;
import com.example.ostb_movie.entity.Cart;
import com.example.ostb_movie.entity.Order;
import com.example.ostb_movie.repository.CartRepository;
import com.example.ostb_movie.service.CartService;
import com.example.ostb_movie.service.KakaoPayService;
import com.example.ostb_movie.service.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KakaoPayController {
	private final KakaoPayService kakaoPayService;
	private final CartService cartService;
	private final OrderService orderService;
	@GetMapping("/pay/ready")
	@ResponseBody
	public KakaoPayReadyDto kakaoPay(HttpSession session, Model model, Principal principal,
			@RequestParam(value = "selectedItems[]", required = false) List<String> selectedItems,@RequestParam(value = "totalprice", required = false) Long totalprice ) {
		System.err.println(totalprice + "jjjjjjjjjjjj");
		
		// 선택된 상품 정보 출력
		Map<String, Object> params = new HashMap<>();
		Long totalPrice = totalprice;
		params.put("totalPrice", totalPrice);
		int conut = 0;
		if (selectedItems != null) {
			for (String itemId : selectedItems) {
				Cart cart = cartService.getCartItemById(Long.parseLong(itemId));
				if (conut == 0) {
					params.put("itemName", cart.getItemId().getItemNm());
				}
				conut += 1;
			}
			params.put("itemCount", conut - 1);
		}
		String tid = (String) session.getAttribute("tid");

		KakaoPayReadyDto res = kakaoPayService.kakaoPay(params);
		// 주문 정보 생성 및 연결
		Order order = new Order();
		order.setOrderStatus(OrderStatus.CANCLE); // 예시: 주문 상태 설정

		// tid 값을 세션에 저장
		session.setAttribute("tid", res.getTid());
		session.setAttribute("selectedItems", selectedItems);

		// tid 값을 success 페이지로 전달하기 위해 모델에 추가
		model.addAttribute("tid", res.getTid());
		model.addAttribute("selectedItems", selectedItems);
		return res;
	}

	@GetMapping("/pay/success")
	public String success(@RequestParam("pg_token") String pgToken, HttpSession session, Principal principal) {
		String tid = (String) session.getAttribute("tid");
		List<String> selectedItems = (List<String>) session.getAttribute("selectedItems");
		
		for (String itemId : selectedItems) {
			Cart cart = cartService.getCartItemById(Long.parseLong(itemId));
			orderService.cartOrder(cart, cart.getEmail());
			cartService.deletcart(Long.parseLong(itemId));
			
		}
		// 카카오 결재 요청하기
		KakaoPayApproveDto kakaoPayApproveDto = kakaoPayService.kakaoPayApprove(tid, pgToken);
		session.removeAttribute("tid");
		session.removeAttribute("params");
		return "/";
	}

	private String generateRandomPayNo() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] randomBytes = new byte[16];
		secureRandom.nextBytes(randomBytes);
		return new String(randomBytes);
	}

}