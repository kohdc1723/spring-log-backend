package org.example.springlogbackend.dto.auth.oauth2;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");

        return email != null ? email.toString() : null;
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("picture").toString();
    }
}
