package com.example.ostb_movie.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.ostb_movie.auth.GoogleUserInfo;
import com.example.ostb_movie.auth.KakaoMemberInfo;
import com.example.ostb_movie.auth.NaverUserInfo;
import com.example.ostb_movie.auth.OAuth2UserInfo;
import com.example.ostb_movie.auth.PrincipalDetails;
import com.example.ostb_movie.constant.Role;
import com.example.ostb_movie.entity.Member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	private final com.example.ostb_movie.repository.MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	
	 OAuth2User oAuth2User = super.loadUser(userRequest);
	 OAuth2UserInfo oAuth2UserInfo = null;

     String provider = userRequest.getClientRegistration().getRegistrationId();    //google
    
     if(provider.equals("google")){
         oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
     }else if(provider.equals("naver")){
    	 System.out.println(oAuth2User.getAttributes() + "JJJJJJJJJJJ");
         oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
         System.out.println("GGGGGGGG");
     }else if(provider.equals("kakao")){	//추가
         oAuth2UserInfo = new KakaoMemberInfo(oAuth2User.getAttributes());
     }
     
     String providerId = oAuth2UserInfo.getProviderId();
     String username = oAuth2UserInfo.getName(); 	
//     String username = oAuth2User.getAttribute("name");  	
     String uuid = UUID.randomUUID().toString().substring(0, 6);
     //String password = passwordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다
     String password = "SNS 로그인";  // 사용자가 입력한 적은 없지만 만들어준다
	 
//     String email = oAuth2User.getAttribute("email");
     String email = oAuth2UserInfo.getEmail();
     Role role = Role.USER;
     
     Member member = memberRepository.findByEmail(email);
     
     //DB에 없는 사용자라면 회원가입처리
     if(member == null){
    	 member = Member.oauth2Register()
        		 .email(email).name(username).password(password).role(role)
                 .provider(provider).providerId(providerId)
                 .build();
//         memberRepository.save(byUsername);
     }
     
     
     return new PrincipalDetails(member, oAuth2UserInfo);
	}
}
