package com.ecommerce.service;

import com.ecommerce.entity.User;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class RefreshTokenService {
    public String createRefreshToken(User user) {
        return UUID.randomUUID().toString();
    }
}
