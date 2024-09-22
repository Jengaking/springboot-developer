package me.leewonjun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.leewonjun.springbootdeveloper.config.jwt.TokenProvider;
import me.leewonjun.springbootdeveloper.domain.User;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private  final TokenProvider tokenProvider;
    private  final RefreshTokenService refreshTokenService;
    private  final UserService userService;

    // 리프레시 토큰을 전달받아 새로운 액세스 토큰을 발급하는 서비스
    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
