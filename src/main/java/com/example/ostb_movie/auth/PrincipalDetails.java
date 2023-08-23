package com.example.ostb_movie.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.ostb_movie.entity.Member;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User{
	private Member member;
	private OAuth2UserInfo oAuth2UserInfo;
	
	public PrincipalDetails(Member member) {
		this.member = member;
	}
	
	public PrincipalDetails(Member member, OAuth2UserInfo oAuth2UserInfo) {
		this.member = member;
		this.oAuth2UserInfo = oAuth2UserInfo;
	}
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
       
        collection.add(new SimpleGrantedAuthority(member.getRole().toString()));

        return collection;
    }
	
	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2UserInfo.getAttributes();
	}

	@Override
	public String getName() {
		return oAuth2UserInfo.getProviderId();
	}
	
}
