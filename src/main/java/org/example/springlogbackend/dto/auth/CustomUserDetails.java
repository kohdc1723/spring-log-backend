package org.example.springlogbackend.dto.auth;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {
    private final User user;
    private final Account account;

    @Override
    @Nonnull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return this.account.getPassword();
    }

    @Override
    @Nonnull
    public String getUsername() {
        return user.getEmail();
    }

    public String getUserId() {
        return user.getId();
    }

    public String getAccountId() {
        return account.getId();
    }
}
