package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.entity.RefreshToken;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.repository.RefreshTokenRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.example.springlogbackend.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void addRefreshToken(String token) {
        String subject = jwtUtil.getSubject(token);

        User user = userRepository.getReferenceById(subject);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
