package org.example.springlogbackend.dto.auth;

import org.example.springlogbackend.entity.ProviderType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImageUrl();

    static OAuth2UserInfo of(
            ProviderType providerType,
            Map<String, Object> attributes
    ) throws OAuth2AuthenticationException {
        return switch (providerType) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case GITHUB -> new GithubOAuth2UserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("Unsupported provider type");
        };
    }
}
