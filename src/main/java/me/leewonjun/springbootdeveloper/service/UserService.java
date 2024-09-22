package me.leewonjun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.leewonjun.springbootdeveloper.domain.User;
import me.leewonjun.springbootdeveloper.dto.AddUserRequest;
import me.leewonjun.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // Bean으로 등록해 줌. WebSecurityConfig

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {return new IllegalAccessError("Unexpected user");});
    }
}