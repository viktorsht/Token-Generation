package com.tokenmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_value", nullable = false, unique = true, length = 255)
    private String tokenValue;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public Token(String tokenValue, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.tokenValue = tokenValue;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(this.expiresAt);
    }

    public long getSecondsRemaining() {
        if (!isValid()) return 0;
        return java.time.Duration.between(LocalDateTime.now(), this.expiresAt).getSeconds();
    }
}
