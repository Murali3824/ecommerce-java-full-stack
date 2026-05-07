package com.ecommerce.service;

import com.ecommerce.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String getUsernameFromToken(String token) {
        return jwtTokenProvider.extractUsername(token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtTokenProvider.validateToken(token, userDetails);
    }
}
