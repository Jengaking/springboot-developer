package me.leewonjun.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.leewonjun.springbootdeveloper.config.jwt.TokenProvider;
import me.leewonjun.springbootdeveloper.config.oauth.OAuth2SuccessHandler;
import me.leewonjun.springbootdeveloper.config.oauth.OAuth2UserCustomService;
import me.leewonjun.springbootdeveloper.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.leewonjun.springbootdeveloper.repository.RefreshTokenRepository;
import me.leewonjun.springbootdeveloper.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@RequiredArgsConstructor
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(
                        new AntPathRequestMatcher("/img/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**")
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 토큰 기반 인증을 위해 기존 폼로그인과 세션 비활성화
        return http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 헤더를 확인할 커스텀 필터 추가
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //토큰 재발급 URL은 인증 없이 접근 가능하도록 설정.
                .authorizeRequests(auth -> auth
                                .requestMatchers(new AntPathRequestMatcher("/api/token")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                                .anyRequest().permitAll())
                .oauth2Login(oauth2 ->
                        oauth2.loginPage("/login")
                                // Authorization 요청과 관련된 상태 저장
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService))
                                        // 인증 성공 시 실행할 핸들러
                                .successHandler(oAuth2SuccessHandler())
                )
                // api로 시작하는 url인 경우 401 상태코드를 반환하도록 예외 처리
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        ))
                .build();

    }

    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
