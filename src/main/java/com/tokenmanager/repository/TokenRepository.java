package com.tokenmanager.repository;

import com.tokenmanager.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    // Tokens válidos (expira no futuro)
    @Query("SELECT t FROM Token t WHERE t.expiresAt > :now ORDER BY t.createdAt DESC")
    List<Token> findValidTokens(@Param("now") LocalDateTime now);

    // Tokens inválidos (já expiraram)
    @Query("SELECT t FROM Token t WHERE t.expiresAt <= :now ORDER BY t.createdAt DESC")
    List<Token> findExpiredTokens(@Param("now") LocalDateTime now);

    // Todos os tokens ordenados por criação
    List<Token> findAllByOrderByCreatedAtDesc();

    // Contagem de válidos
    @Query("SELECT COUNT(t) FROM Token t WHERE t.expiresAt > :now")
    long countValidTokens(@Param("now") LocalDateTime now);

    // Contagem de inválidos
    @Query("SELECT COUNT(t) FROM Token t WHERE t.expiresAt <= :now")
    long countExpiredTokens(@Param("now") LocalDateTime now);
}
