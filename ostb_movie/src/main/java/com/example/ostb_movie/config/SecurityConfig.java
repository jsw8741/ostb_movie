package com.example.ostb_movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.ostb_movie.service.CustomOAuth2SuccessHandler;
import com.example.ostb_movie.service.PrincipalOauth2UserService;


@Configuration // bean 객체를 싱글톤으로 공유할 수 있게 해준다.
@EnableWebSecurity // spring security filterCahin이 자동으로 포함되게한다.
public class SecurityConfig {
	
	private final PrincipalOauth2UserService principalOauth2UserService;
	
	
	public SecurityConfig(PrincipalOauth2UserService principalOauth2UserService) {
		this.principalOauth2UserService = principalOauth2UserService;
	}
	
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}
	
	@Bean
    public AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new CustomOAuth2SuccessHandler();
    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

		http
		
		.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(mvc.pattern("/"), mvc.pattern("/login/**"), mvc.pattern("/oauth/**")).permitAll()
				.requestMatchers(mvc.pattern("/css/**"), mvc.pattern("/js/**"), mvc.pattern("/img/**"), mvc.pattern("/images/**"), mvc.pattern("/fonts/**")).permitAll()
				.requestMatchers(mvc.pattern("/favicon"), mvc.pattern("/error"), mvc.pattern("api/**")).permitAll()
				.requestMatchers(mvc.pattern("/test")).hasRole("USER")
	            .anyRequest().authenticated())		
		.formLogin(formLogin -> formLogin //2. 로그인에 관련된 설정
				.loginPage("/login/loginForm") //로그인 페이지 URL 설정
				.defaultSuccessUrl("/") //로그인 성공시 이동할 페이지
				.usernameParameter("email") //로그인시 id로 사용할 파라메터 이름
				.failureUrl("/login/error") //로그인 실패시 이동할 URL
				)
		.oauth2Login(oauth2 -> oauth2 
				.loginPage("/login/loginForm")
				.successHandler(oauth2AuthenticationSuccessHandler())
//				.defaultSuccessUrl("/")
				.failureUrl("/login/error")
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
				.userService(principalOauth2UserService))
				) 
		.logout(logout -> logout //3. 로그아웃에 관련된 설정
				.logoutRequestMatcher(new AntPathRequestMatcher("/login/logout")) //로그아웃시 이동할 URL
				.logoutSuccessUrl("/") //로그아웃 성공시 이동할 URL
				)   //4.인증되지 않은 사용자가 리소스에 접근했을때 설정(ex. 로그인 안했는데 cart페이지에 접근..)
		.exceptionHandling(handling -> handling
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				)
		.rememberMe(Customizer.withDefaults());
		
		
		
		return http.build();
	}
}