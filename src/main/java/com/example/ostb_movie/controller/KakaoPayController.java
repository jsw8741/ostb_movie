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
import com.example.ostb_movie.service.KakaoPayService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class KakaoPayController {
	private final KakaoPayService kakaoPayService;
	
	@GetMapping("/pay/ready")
	@ResponseBody
	public KakaoPayReadyDto kakaoPay(@ModelAttribute OrderDto orderDto,  Model model, HttpSession session, Principal principal) {
		Map<String, Object> params = new HashMap<>();
	    params.put("cartItemid", orderDto.getItemId());
	    
		KakaoPayReadyDto res = kakaoPayService.kakaoPay(params);
		
	    System.out.println("1111111111111111");
	    // 주문 정보 생성 및 연결
	    Order order = new Order();
	    order.setOrderStatus(OrderStatus.CANCLE); // 예시: 주문 상태 설정
	    System.out.println("222222222222222222");
		
	    // tid 값을 세션에 저장
	    session.setAttribute("tid", res.getTid());
	    System.out.println("33333333333333333333");

	    // tid 값을 success 페이지로 전달하기 위해 모델에 추가
	    model.addAttribute("tid", res.getTid());
	    System.out.println("44444444444444444444444444");
		return res;
	}
	
	@GetMapping("/pay/success")
	public String success(@RequestParam("pg_token")String pgToken,HttpSession session, @ModelAttribute("cart") Cart cart, Principal principal) {
			String tid = (String) session.getAttribute("tid");
			System.out.println("cart정보= " + cart);
	        // 카카오 결재 요청하기
	        KakaoPayApproveDto kakaoPayApproveDto = kakaoPayService.kakaoPayApprove(tid, pgToken);
	        System.out.println(kakaoPayApproveDto);
	        

	        return "/";
	}
	
	
	private String generateRandomPayNo() {
	    SecureRandom secureRandom = new SecureRandom();
	    byte[] randomBytes = new byte[16];
	    secureRandom.nextBytes(randomBytes);
	    return new String(randomBytes);
	}
	
	
}