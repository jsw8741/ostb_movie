package com.example.ostb_movie.controller;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ostb_movie.dto.KakaoPayApproveDto;
import com.example.ostb_movie.dto.KakaoPayReadyDto;
import com.example.ostb_movie.dto.OrderDto;
import com.example.ostb_movie.service.KakaoPayService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class KakaoPayController {
	private final KakaoPayService kakaoPayService;
	
	@GetMapping("/pay/ready")
	@ResponseBody
	public KakaoPayReadyDto kakaoPay(@ModelAttribute  OrderDto orderDto, Model model, HttpSession session) {
		
		Map<String, Object> params = new HashMap<>();
	    params.put("itemName", orderDto.getItemId());
	    params.put("totalPrice", orderDto.getTotalprice());
	    
	    System.out.println(params);
		KakaoPayReadyDto res = kakaoPayService.kakaoPay(params);
	    // tid 값을 세션에 저장
	    session.setAttribute("tid", res.getTid());

	    // tid 값을 success 페이지로 전달하기 위해 모델에 추가
	    model.addAttribute("tid", res.getTid());
		return res;
	}
	
	@GetMapping("/pay/success")
	public String success(@RequestParam("pg_token")String pgToken,HttpSession session, Model model) {
			System.out.println("진짜 왜이래");
			String tid = (String) session.getAttribute("tid");
	        // 카카오 결재 요청하기
	        KakaoPayApproveDto kakaoPayApproveDto = kakaoPayService.kakaoPayApprove(tid, pgToken);
		return "/main";
	}
	
	
}