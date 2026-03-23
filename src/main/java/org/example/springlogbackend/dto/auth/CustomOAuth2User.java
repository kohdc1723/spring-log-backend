package org.example.springlogbackend.dto.auth;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.UserRoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class CustomOAuth2User implements OAuth2User {
    private final String userId;
    private final String email;
    private final String accountId;
    private final UserRoleType role;
    private final ProviderType provider;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(Account account, Map<String, Object> attributes) {
        this.userId = account.getUser().getId();
        this.email = account.getUser().getEmail();
        this.accountId = account.getId();
        this.role = account.getUser().getRole();
        this.provider = account.getProvider();
        this.attributes = attributes;
    }

    public static CustomOAuth2User of(Account account, Map<String, Object> attributes) {
        return new CustomOAuth2User(account, attributes);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    @Nonnull
    public String getName() {
        return this.userId;
    }
}
