package org.example.springlogbackend.dto.auth;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GithubOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");

        return email != null ? email.toString() : null;
    }

    @Override
    public String getName() {
        Object name = attributes.get("name");

        if (name != null) return name.toString();

        return attributes.get("login").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("avatar_url").toString();
    }
}
