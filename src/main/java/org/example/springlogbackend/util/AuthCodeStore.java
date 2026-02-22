package org.example.springlogbackend.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.springlogbackend.entity.ProviderType;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AuthCodeStore {
    private final Cache<String, String> storage = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(1000)
            .build();

    public String generate(String accountId) {
        String code = UUID.randomUUID().toString();
        storage.put(code, accountId);

        return code;
    }

    public Optional<String> consume(String code) {
        String accountId = storage.getIfPresent(code);

        if (accountId == null) {
            return Optional.empty();
        }

        storage.invalidate(code);

        return Optional.of(accountId);
    }
}
