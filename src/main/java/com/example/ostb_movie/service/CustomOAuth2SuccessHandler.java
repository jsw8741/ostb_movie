package com.example.ostb_movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        
        
        String targetUrl = determineTargetUrl(member);
        response.sendRedirect(targetUrl);
	}
	
    private String determineTargetUrl(Member member) {
    	
    	Member findMember = memberRepository.findByEmail(member.getEmail());
    	
        if (findMember == null) {
            return "/login/joinSNSForm";
        } else {
            return "/";
        }
    }

	
}
