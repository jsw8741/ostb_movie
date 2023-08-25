package com.example.ostb_movie.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KakaoPay {

	@PostMapping(value = "/kakao")
	@ResponseBody
	public String kakaopay(@RequestParam("totalPrice") String totalPrice, @RequestParam("tototo") String tototo) {
		try {
			URL kakaoUrl = new URL("https://kapi.kakao.com/v1/payment/ready");
			try {
				HttpURLConnection conServer = (HttpURLConnection) kakaoUrl.openConnection();
				conServer.setRequestMethod("POST");
				conServer.setRequestProperty("Authorization", "KakaoAK 826f9676e26b9186cb6767cddf17d62b");
				conServer.addRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
				conServer.setDoOutput(true);
				String param = "cid=TC0ONETIME" // 가맹점 코드
						+ "&partner_order_id=partner_order_id" // 가맹점 주문번호
						+ "&partner_user_id=partner_user_id" // 가맹점 회원 id
						+ "&item_name=movie" // 상품명
						+ "&quantity=1" // 상품 수량
						+ "&total_amount=" + totalPrice // 총 금액
						+ "&vat_amount=200" // 부가세
						+ "&tax_free_amount=0" // 상품 비과세 금액
						+ "&approval_url=https://developers.kakao.com/success" // 결제 성공 시
						+ "&fail_url=https://developers.kakao.com/fail" // 결제 실패 시
						+ "&cancel_url=https://developers.kakao.com/cancel"; // 결제 취소 시
				OutputStream outPut = conServer.getOutputStream();
				DataOutputStream dataOutPut = new DataOutputStream(outPut);
				dataOutPut.writeBytes(param);
				dataOutPut.close();

				int result = conServer.getResponseCode();

				InputStream dataInput;
				if (result == 200) {
					dataInput = conServer.getInputStream();
				} else {
					dataInput = conServer.getErrorStream();
				}

				InputStreamReader reader = new InputStreamReader(dataInput);
				BufferedReader buffer = new BufferedReader(reader);
				return buffer.readLine();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String resultMessage = "결제 성공."; // 예시 메시지

		return resultMessage;
	}

}