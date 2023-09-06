package com.example.ostb_movie.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.constant.PaymentStatus;
import com.example.ostb_movie.dto.*;
import com.example.ostb_movie.entity.*;
import com.example.ostb_movie.service.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookController {
	private final TheaterService theaterService;
	private final MovieService movieService;
	private final BookService bookService;
	private final PaymentService paymentService;
	private final KakaoPayService kakaoPayService;

	// 상영시간표 페이지
	@GetMapping(value = "/movie/schedule")
	public String schedule(Model model, @RequestParam(required = false) String viewDate) {
		LocalDate date;

		// viewDate 파라미터가 있으면 그 값 사용
		if (viewDate != null) {
			date = LocalDate.parse(viewDate);
		} else {
			// 없으면 현재 날짜 사용
			date = LocalDate.now();
		}

		// 상영스케줄 조회하고 조회 결과 모델에 추가
		List<Theater> schedules = theaterService.getTheaterSchedulesByDate(date, true);
		model.addAttribute("schedules", schedules);
		return "book/schedule";
	}

	// 상영정보 등록 페이지
	@GetMapping(value = "/admin/theater/new")
	public String theaterForm(Model model) {
		List<Movie> movies = movieService.getMovieAll();
		model.addAttribute("movies", movies);
		model.addAttribute("theaterFormDto", new TheaterFormDto());
		return "book/theaterForm";
	}

	// 상영정보 등록 - insert
	@PostMapping(value = "/admin/theater/new")
	public String submitTheaterForm(TheaterFormDto theaterFormDto, Model model) {
		try {
			theaterService.saveTheater(theaterFormDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "상영정보 등록 중 에러가 발생했습니다.");
			return "book/theaterForm";
		}
		return "redirect:/";
	}

	// 상영정보 수정 페이지
	@GetMapping(value = "/admin/theater/{theaterId}")
	public String theaterDtl(@PathVariable("theaterId") Long theaterId, Model model) {

		try {
			List<Movie> movies = movieService.getMovieAll();
			TheaterFormDto theaterFormDto = theaterService.getTheaterDtl(theaterId);
			model.addAttribute("movies", movies);
			model.addAttribute("theaterFormDto", theaterFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상영정보를 가져오는 도중 에러가 발생했습니다.");
			model.addAttribute("theaterFormDto", new TheaterFormDto());
			return "book/theaterForm";
		}

		return "book/theaterModifyForm";
	}

	// 상영정보 수정 - update
	@PostMapping(value = "/admin/theater/{theaterId}")
	public String theaterUpdate(TheaterFormDto theaterFormDto, Model model) {
		try {
			theaterService.updateTheater(theaterFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상영정보 수정 중 에러가 발생했습니다.");
			return "book/theaterModifyForm";
		}
		return "redirect:/";
	}

	// 상영정보 삭제 - delete
	@GetMapping(value = "/admin/theater/delete/{theaterId}")
	public String deleteTheater(@PathVariable("theaterId") Long theaterId) {
		theaterService.deleteTheater(theaterId);
		return "redirect:/admin/theaterMng";
	}

	// 상영정보 관리 페이지
	@GetMapping(value = "/admin/theaterMng")
	public String theaterMng(@RequestParam(required = false) LocalDate viewDate, Model model) {
		if (viewDate == null) {
			viewDate = LocalDate.now();
		}

		List<Theater> schedules = theaterService.getTheaterSchedulesByDate(viewDate, true);
		model.addAttribute("schedules", schedules);
		model.addAttribute("viewDate", viewDate);
		return "book/theaterMng";
	}

	// 티켓예매 페이지
	@GetMapping(value = "/book/ticketing")
	public String ticketing(Model model, @RequestParam(required = false) String viewDate,
			Authentication authentication) {
		LocalDate date;

		// viewDate 파라미터가 있으면 그 값 사용
		if (viewDate != null) {
			date = LocalDate.parse(viewDate);
		} else {
			// 없으면 현재 날짜 사용
			date = LocalDate.now();
		}

		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();
		System.out.println(member);

		// 상영스케줄 조회하고 조회 결과 모델에 추가
		List<Theater> schedules = theaterService.getTheaterSchedulesByDate(date, true);
		model.addAttribute("member", member);
		model.addAttribute("schedules", schedules);
		return "book/ticketing";
	}

	// 티켓예매 - insert
	/*
	 * @PostMapping(value = "/book/ticketing")
	 * 
	 * @ResponseBody public Map<String, Object> submitTicketing(@RequestBody
	 * Map<String, Object> paramMap, Model model, Principal principal) { Map<String,
	 * Object> resultMap = new HashMap<String, Object>(); try { Book book =
	 * bookService.submitTicketing(paramMap);
	 * 
	 * Member member = new Member();
	 * member.setId(Long.parseLong(paramMap.get("memberId").toString()));
	 * 
	 * Payment payment = new Payment(); payment.setPaymentDate(LocalDateTime.now());
	 * payment.setPaymentMethode("카카오페이");
	 * payment.setPaymentStatus(PaymentStatus.COMPLETE); payment.setBook(book);
	 * payment.setMember(member);
	 * 
	 * paymentService.savePayment(payment);
	 * 
	 * resultMap.put("book", book); } catch (Exception e) { e.printStackTrace(); }
	 * return resultMap; }
	 */

	// 카카오페이
	@GetMapping("/bookpay/ready")
	@ResponseBody
	public KakaoPayReadyDto kakaoPayReady(HttpSession session, Model model, Principal principal,
			@RequestParam Map<String, Object> paramMap) {

		Map<String, Object> params = new HashMap<>();

		Long theaterId = Long.parseLong(String.valueOf(paramMap.get("theaterId")));
		Theater theater = theaterService.getTheater(theaterId);

		Long totalPrice = Long.parseLong(String.valueOf(paramMap.get("totalPrice")));

		int count = Integer.parseInt(String.valueOf(paramMap.get("seatCnt")));

		params.put("itemName", theater.getMovie().getMovieTitle());
		params.put("totalPrice", totalPrice);
		params.put("itemCount", count);
		params.put("int", "1");
		String tid = (String) session.getAttribute("tid");

		KakaoPayReadyDto res = kakaoPayService.kakaoPay(params);

		Payment payment = new Payment();
		payment.setPaymentStatus(PaymentStatus.CANCEL);

		session.setAttribute("tid", res.getTid());
		session.setAttribute("paramMap", paramMap);

		model.addAttribute("tid", res.getTid());
		model.addAttribute("paramMap", paramMap);
		return res;

	}

	// 결제 성공 시
	@GetMapping("/bookpay/success")
	public String success(@RequestParam("pg_token") String pgToken, HttpSession session, RedirectAttributes redirectAttributes) {
		String tid = (String) session.getAttribute("tid");
		Map<String, Object> paramMap = (Map<String, Object>) session.getAttribute("paramMap");
		Book book = bookService.submitTicketing(paramMap);
		Member member = new Member();
		member.setId(Long.parseLong(paramMap.get("memberId").toString()));
		
		Payment payment = new Payment();
		payment.setPaymentDate(LocalDateTime.now());
		payment.setPaymentMethode("카카오페이");
		payment.setPaymentStatus(PaymentStatus.COMPLETE);
		payment.setBook(book);
		payment.setMember(member);
		
		paymentService.savePayment(payment);
		
	    int price = payment.getBook().getPrice();
	    Long currentPoint = member.getPoint() != null ? member.getPoint() : 0L;
	    Long totalPay = member.getTotalPay() != null ? member.getTotalPay() : 0L;

	    // 적립 포인트 계산(일단 10퍼센트로 설정)
	    Long newPoint = currentPoint + (price / 10);
	    
	    // 누적 결제 금액
	    Long newTotalPay = totalPay + price;

	    // 포인트, 누적 결제 금액 변경
	    member.setPoint(newPoint);
	    member.setTotalPay(newTotalPay);
		
		KakaoPayApproveDto kakaoPayApproveDto = kakaoPayService.kakaoPayApprove(tid, pgToken);

		session.removeAttribute("tid");
		session.removeAttribute("params");
		
		redirectAttributes.addFlashAttribute("successMessage", "예매가 완료되었습니다.");

		return "redirect:/members/pay/success";

	}
	
	@PostMapping("/book/{paymentId}/cancel")
	public @ResponseBody ResponseEntity cancelPayment(@PathVariable("paymentId") Long paymentId, Authentication authentication) {
		paymentService.cancelPayment(paymentId);
		return new ResponseEntity<Long>(paymentId, HttpStatus.OK);
	}

	// 예매된 좌석정보 조회
	@PostMapping(value = "/book/getSeatData")
	@ResponseBody
	public Map<String, Object> getSeatData(@RequestBody Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<String> originalList = bookService.getSeatList(paramMap.get("theaterId").toString());

			List<Integer> cleanedList = new ArrayList<>();
			for (String seatNumbers : originalList) {
				String[] splitNumbers = seatNumbers.split(", ");
				for (String numStr : splitNumbers) {
					try {
						int num = Integer.parseInt(numStr);
						cleanedList.add(num);
					} catch (NumberFormatException e) {
						// 숫자로 변환할 수 없는 경우 무시
					}
				}
			}

			// 숫자를 오름차순으로 정렬
			Collections.sort(cleanedList);

			// 다시 문자열로 변환
			List<String> cleanedStringList = new ArrayList<>();
			for (int num : cleanedList) {
				cleanedStringList.add(String.valueOf(num));
			}

			resultMap.put("list", cleanedStringList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	// 예매내역 조회 페이지
	@GetMapping(value = "/members/reserv")
	public String bookingHist(Model model, Authentication authentication) {
		if (authentication == null) {
			model.addAttribute("errorMessage", "로그인 후 이용해 주세요.");
			return "redirect:/login/loginForm";
		}

		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();
		String email = member.getEmail();

		List<Payment> completed = paymentService.getCompleteList(email); // 결제완료목록
		List<Payment> canceled = paymentService.getCancelList(email); // 결제취소목록

		model.addAttribute("completed", completed);
		model.addAttribute("canceled", canceled);
		return "book/history";
	}
	
	@GetMapping(value = "/members/pay/success")
	public String paySuccess() {
		
		return "/paySuccess";
	}

}