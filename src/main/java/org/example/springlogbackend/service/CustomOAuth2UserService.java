package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springlogbackend.dto.auth.oauth2.CustomOAuth2User;
import org.example.springlogbackend.dto.auth.oauth2.GithubOAuth2UserInfo;
import org.example.springlogbackend.dto.auth.oauth2.OAuth2UserInfo;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.entity.UserRoleType;
import org.example.springlogbackend.repository.AccountRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final RestClient restClient;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        ProviderType provider = ProviderType.valueOf(registrationId);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(provider, attributes);

        if (provider == ProviderType.GITHUB && oAuth2UserInfo.getEmail() == null) {
            String email = fetchGitHubPrimaryEmail(userRequest);

            if (email == null) {
                throw new OAuth2AuthenticationException("GitHub primary email is required");
            }

            oAuth2UserInfo = new GithubOAuth2UserInfo(oAuth2User.getAttributes(), email);
        }

        Account account = processOAuth2Login(provider, oAuth2UserInfo);

        return new CustomOAuth2User(account, attributes);
    }

    private Account processOAuth2Login(ProviderType provider, OAuth2UserInfo oAuth2UserInfo) {
        return accountRepository
                .findByProviderAndProviderId(provider, oAuth2UserInfo.getProviderId())
                .orElseGet(() -> linkOrCreateAccount(provider, oAuth2UserInfo));
    }

    private Account linkOrCreateAccount(ProviderType provider, OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();

        User user = userRepository.findByEmailAndDeleted(email, false)
                .orElseGet(() -> createUser(email));

        Account account = Account.builder()
                .user(user)
                .provider(provider)
                .providerId(oAuth2UserInfo.getProviderId())
                .build();

        return accountRepository.save(account);
    }

    private User createUser(String email) {
        User user = User.builder()
                .email(email)
                .role(UserRoleType.USER)
                .build();

        return userRepository.save(user);
    }

    private String fetchGitHubPrimaryEmail(OAuth2UserRequest oAuth2UserRequest) {
        try {
            String token = oAuth2UserRequest.getAccessToken().getTokenValue();

            List<Map<String, Object>> emails = restClient.get()
                    .uri("https://api.github.com/user/emails")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (emails == null) return null;

            return emails.stream()
                    .filter(e -> Boolean.TRUE.equals(e.get("primary"))
                            && Boolean.TRUE.equals(e.get("verified")))
                    .map(e -> e.get("email").toString())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.warn("Failed to fetch GitHub primary email: {}", e.getMessage());
            return null;
        }
    }
}
