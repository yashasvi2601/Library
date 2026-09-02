package com.MiniProject.Library_Management.service;

import com.MiniProject.Library_Management.model.BlacklistedToken;
import com.MiniProject.Library_Management.repository.BlacklistedTokenRepository;
import com.MiniProject.Library_Management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final BlacklistedTokenRepository blacklistRepo;
    private final JwtService jwtService;

    public void logout(String token) {
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(jwtService.extractExpiration(token));

        blacklistRepo.save(blacklistedToken);
    }
}

