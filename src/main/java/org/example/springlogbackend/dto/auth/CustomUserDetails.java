package org.example.springlogbackend.dto.auth;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
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
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getUserId() {
        return user.getId();
    }
}
