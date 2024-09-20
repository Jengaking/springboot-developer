package me.leewonjun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.leewonjun.springbootdeveloper.domain.User;
import me.leewonjun.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository; // Autowired?


    @Override
    public User loadUserByUsername(String email) { // AuthenticationProviders에게 User 정보를 전달함.
        return userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException((email)));
    }
}
