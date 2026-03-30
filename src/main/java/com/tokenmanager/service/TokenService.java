package com.tokenmanager.service;

import com.tokenmanager.model.Token;
import com.tokenmanager.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;

    private static final int TOKEN_VALIDITY_MINUTES = 5;

    @Transactional
    public Token generateToken() {
        String tokenValue = generateUniqueTokenValue();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(TOKEN_VALIDITY_MINUTES);

        Token token = new Token(tokenValue, now, expiresAt);
        Token saved = tokenRepository.save(token);
        log.info("Token gerado: {} | Expira em: {}", saved.getTokenValue(), saved.getExpiresAt());
        return saved;
    }

    public List<Token> getAllTokens() {
        return tokenRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Token> getValidTokens() {
        return tokenRepository.findValidTokens(LocalDateTime.now());
    }

    public List<Token> getExpiredTokens() {
        return tokenRepository.findExpiredTokens(LocalDateTime.now());
    }

    public Map<String, Object> getStats() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", tokenRepository.count());
        stats.put("valid", tokenRepository.countValidTokens(now));
        stats.put("expired", tokenRepository.countExpiredTokens(now));
        return stats;
    }

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("allTokens", getAllTokens());
        data.put("validTokens", getValidTokens());
        data.put("expiredTokens", getExpiredTokens());
        data.put("stats", getStats());
        return data;
    }

    private String generateUniqueTokenValue() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
