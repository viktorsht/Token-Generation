package com.tokenmanager.scheduler;

import com.tokenmanager.model.Token;
import com.tokenmanager.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenScheduler {

    private final TokenService tokenService;

    /**
     * Gera um novo token a cada 30 segundos
     */
    @Scheduled(fixedRate = 30000)
    public void generateTokenPeriodically() {
        try {
            Token token = tokenService.generateToken();
            log.info("[SCHEDULER] Novo token gerado automaticamente: {}", token.getTokenValue());
        } catch (Exception e) {
            log.error("[SCHEDULER] Erro ao gerar token: {}", e.getMessage(), e);
        }
    }
}
