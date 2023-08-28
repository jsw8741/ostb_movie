package com.example.ostb_movie.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.ostb_movie.dto.KakaoPayApproveDto;
import com.example.ostb_movie.dto.KakaoPayReadyDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

	public KakaoPayReadyDto kakaoPay(Map<String, Object> params) {
		System.out.println("5555555555555555555555555");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK a729a14eaa765b52faa416faff03f519");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<String, Object>();
		
		System.out.println("666666666666666666666666666666");
		payParams.add("cid", "TC0ONETIME");
		payParams.add("partner_order_id", "partner_order_id");
		payParams.add("partner_user_id", "partner_user_id");
		payParams.add("item_name", params.get("itemName"));
		payParams.add("quantity", "1");
		payParams.add("total_amount", params.get("totalPrice"));
		payParams.add("tax_free_amount", "0");
		payParams.add("approval_url", "http://localhost/pay/success");
		payParams.add("cancel_url", "http://localhost/pay/cancel");
		payParams.add("fail_url", "http://localhost/pay/fail");

		System.out.println("777777777777777777777777777");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(payParams, headers);
		
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
		System.out.println("8888888888888888888888888888");
		KakaoPayReadyDto res = template.postForObject(url, request, KakaoPayReadyDto.class);
		System.out.println("9999999999999999999999");
		
		return res;
	}
	
	
	public KakaoPayApproveDto kakaoPayApprove(String tid, String pgToken) {
		System.out.println("못온거 맞지????");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK a729a14eaa765b52faa416faff03f519");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<String, Object>();
		System.out.println(tid + "dddddddddddddddd");
		
		payParams.add("cid", "TC0ONETIME");
		payParams.add("tid", tid);
		payParams.add("partner_order_id", "partner_order_id");
		payParams.add("partner_user_id", "partner_user_id");
		payParams.add("pg_token", pgToken);
		
		HttpEntity<Map> request = new HttpEntity<Map>(payParams, headers);
		
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/approve";
		
		KakaoPayApproveDto res = template.postForObject(url, request, KakaoPayApproveDto.class);
		
		return res;
	}
}