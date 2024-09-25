package me.leewonjun.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import me.leewonjun.springbootdeveloper.domain.User;
import me.leewonjun.springbootdeveloper.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    // 리소스 서버에서 보내주는 사용자 정보를 불러온다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); // 아마 여기서 request를 리소스 서버로 전송하고 결과를 반환 받는듯
        saveOrUpdate(user); // 그럼 받아온 사용자 정보를 DB에 업데이트 한다.
        return user;
    }

    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String user_email = "";

        if(attributes.containsKey("email")) {
            user_email = (String)attributes.get("email");
        }else {
            user_email = (String)((Map<String, Object>)attributes.get("kakao_account")).get("email");
        }
        String name = (String)attributes.get("name");
        User user = userRepository.findByEmail(user_email)
                .map(entiry -> entiry.update(name)).orElse(User.builder().email(user_email).nickname(name).build());

        return userRepository.save(user);
    }
}
