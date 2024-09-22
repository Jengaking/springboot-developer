package me.leewonjun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.leewonjun.springbootdeveloper.domain.RefreshToken;
import me.leewonjun.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
