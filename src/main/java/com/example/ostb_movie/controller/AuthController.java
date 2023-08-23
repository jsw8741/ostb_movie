package com.example.ostb_movie.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	
    @GetMapping("/check-login")
    public Map<String, Boolean> checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();

        Map<String, Boolean> result = new HashMap<>();
        result.put("isAuthenticated", isAuthenticated);
        return result;
    }
}
