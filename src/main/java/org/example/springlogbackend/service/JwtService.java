package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.dto.auth.oauth2.TokensDto;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.RefreshToken;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.exception.BusinessException;
import org.example.springlogbackend.repository.AccountRepository;
import org.example.springlogbackend.repository.RefreshTokenRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.example.springlogbackend.util.AuthCodeStore;
import org.example.springlogbackend.util.JwtTokenType;
import org.example.springlogbackend.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthCodeStore authCodeStore;
    private final AccountRepository accountRepository;

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

    @Transactional
    public TokensDto generateToken(String code) {
        String accountId = authCodeStore.consume(code)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_AUTH_CODE,
                        "Account not found with auth code: %s".formatted(code)));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        "Account not found with id: %s".formatted(accountId)));

        User user = account.getUser();
        String userId = user.getId();
        String email = user.getEmail();
        String role = "ROLE_" + user.getRole().name();
        ProviderType provider = account.getProvider();

        String accessToken = jwtUtil.createToken(userId, provider, email, role, JwtTokenType.ACCESS);
        String refreshToken = jwtUtil.createToken(userId, provider, email, role, JwtTokenType.REFRESH);

        addRefreshToken(refreshToken);

        return TokensDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public TokensDto refresh(String refreshToken) {
        if (!jwtUtil.isValid(refreshToken, JwtTokenType.REFRESH)) {
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }

        refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        String userId = jwtUtil.getSubject(refreshToken);
        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        ProviderType provider = ProviderType.valueOf(jwtUtil.getProvider(refreshToken));

        refreshTokenRepository.deleteByToken(refreshToken);

        String newAccessToken = jwtUtil.createToken(userId, provider, email, role, JwtTokenType.ACCESS);
        String newRefreshToken = jwtUtil.createToken(userId, provider, email, role, JwtTokenType.REFRESH);

        addRefreshToken(newRefreshToken);

        return TokensDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }
}
