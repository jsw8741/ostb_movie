package com.example.ostb_movie.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.dto.MemberFormDto;
import com.example.ostb_movie.dto.MemberSearchDto;
import com.example.ostb_movie.dto.MypageFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	// 현재 비밀번호 확인 화면
	@GetMapping(value = "/members/checkPw")
	public String checkPwForm() {

		return "member/checkPwPop";
	}

	// 현재 비밀번호 확인
	@PostMapping(value = "/members/checkPw")
	public String checkPw(@RequestParam("password") String password, Authentication authentication, Model model) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();

		if (passwordEncoder.matches(password, member.getPassword())) {

			return "member/modifyPwPop";
		} else {
			model.addAttribute("member", member);
			model.addAttribute("errorMessage", "비밀번호가 다릅니다!");

			return "member/checkPwPop";
		}
	}

	// 비밀번호 변경
	@PostMapping(value = "/members/modifyPw")
	public String modifyPw(@RequestParam("password") String password, Authentication authentication, Model model) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member currentMember = principal.getMember();

		Member member = memberService.findMember(currentMember.getEmail());
		member.setPassword(passwordEncoder.encode(password));
		member = memberService.updatePassword(member);

		if (passwordEncoder.matches(password, member.getPassword())) {
			model.addAttribute("successMessage", "비밀번호 변경이 완료되었습니다.");
			return "member/modifyPwPop";
		} else {
			model.addAttribute("errorMessage", "비밀번호 변경이 실패되었습니다.다시 시도해주세요.");
			return "member/checkPwPop";
		}

	}

	// 마이페이지 화면
	@GetMapping(value = "/members/info")
	public String newMyPage(Authentication authentication, Model model) {

		// 현재 로그인한 회원 정보 가져오기
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();

		Member updateMember = memberService.findMember(member.getEmail());
		model.addAttribute("member", updateMember);

		return "member/myPage";

	}

	// 팝업
	@GetMapping(value = "/members/infoPop")
	public String pop(Authentication authentication, Model model) {

		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		Member member = principal.getMember();

		Member updateMember = memberService.findMember(member.getEmail());

		MypageFormDto mypageFormDto = MypageFormDto.of(member);

		model.addAttribute("member", updateMember);
		model.addAttribute("mypageFormDto", mypageFormDto);

		return "member/myPagePop";
	}

	// (Master)회원 관리창
	@GetMapping(value = { "/admin/members", "/admin/members/{page}" })
	public String memberManage(MemberSearchDto memberSearchDto, @PathVariable("page") Optional<Integer> page,
			Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

		Page<Member> members = memberService.getAdminMemberPage(memberSearchDto, pageable);
		model.addAttribute("members", members);
		model.addAttribute("memberSearchDto", memberSearchDto);
		model.addAttribute("maxPage", 5);

		return "member/memberMng";
	}

	// (Master)회원 수정창
	@GetMapping(value = "/admin/member/{memberId}")
	public String memberDtl(@PathVariable("memberId") Long memberId, Model model) {
		try {
			
			Member member = memberService.MemberId(memberId);
			
			MemberFormDto memberFormDto = MemberFormDto.of(member);
			model.addAttribute("memberid",memberId);
			model.addAttribute("member", memberFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "회원 수정정보를 불러오는중 에러가 발생했습니다.");
			return "redirect:/admin/members/";
		}
		
		return "member/memberModifyMaster";
		}
	
	@PostMapping(value = "/admin/modify/member/{memberId}")
	public String memberUpdate(@Valid MemberFormDto member, @PathVariable("memberId") Long memberId,@RequestParam("plusPoint") Long point, Model model, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "redirect:/admin/member/" + memberId;
		}
		Member member2 =memberService.MemberId(memberId);
		try {
			if(point != null) {
				member.setPoint(point + member2.getPoint());
				
			}else {
				member.setPoint(member2.getPoint());
			}
			memberService.updateAdminMember(member);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errormessage", "회원정보 수정 중 에러가 발생했습니다.");
			return "redirect:/admin/members";

		}
		return "redirect:/";
	}
	
	//팝업
		@GetMapping(value = "/members/infoPop")
		public String pop(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
			
			PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
	        Member member = principal.getMember();
	        
	        Member updateMember = memberService.findMember(member.getEmail());
	        
	        MypageFormDto mypageFormDto = MypageFormDto.of(updateMember);
			model.addAttribute("member",updateMember);
			model.addAttribute("mypageFormDto",mypageFormDto);
			
			return "member/myPagePop";
		}
		
		//마이페이지 수정
		@PostMapping(value = "/members/infoPop")
		public String popProfile(@Valid MypageFormDto mypageFormDto, Authentication authentication,
				Model model, BindingResult bindingResult, 
				@RequestParam("memberImg") MultipartFile memberImgFile
				, RedirectAttributes redirectAttributes) {
			
			if(bindingResult.hasErrors()) {
				return "member/myPagePop";
			}
	        
			if(memberImgFile.isEmpty() && mypageFormDto.getId() == null) {
				model.addAttribute("errorMessage", "상품 이미지는 필수입니다.");
				return "member/myPagePop";
			}
			
			PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
			Member member = principal.getMember();
				
			try {
				if(memberImgFile.isEmpty() || memberImgFile == null) {	
					member = memberService.nickNameUpdate(mypageFormDto, member.getId());
					redirectAttributes.addFlashAttribute("successMessage", "프로필 수정이 완료되었습니다.");
					return "redirect:/members/infoPop";
				} else {
					memberService.updateMember(mypageFormDto, memberImgFile);
					redirectAttributes.addFlashAttribute("successMessage", "프로필 수정이 완료되었습니다.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "프로필 등록 중 에러가 발생했습니다.");
				return "member/myPagePop";
			}
			
			
			
			Member updateMember = memberService.findMember(member.getEmail());
			model.addAttribute("member",updateMember);
			model.addAttribute("mypageFormDto",mypageFormDto);
			return "redirect:/members/infoPop";
		}
}