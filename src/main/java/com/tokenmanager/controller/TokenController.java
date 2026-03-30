package com.tokenmanager.controller;

import com.tokenmanager.model.Token;
import com.tokenmanager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    /**
     * Página principal - Dashboard
     */
    @GetMapping("/")
    public String index(Model model) {
        Map<String, Object> data = tokenService.getDashboardData();
        model.addAttribute("allTokens", data.get("allTokens"));
        model.addAttribute("validTokens", data.get("validTokens"));
        model.addAttribute("expiredTokens", data.get("expiredTokens"));
        model.addAttribute("stats", data.get("stats"));
        return "index";
    }

    // ==================== REST API ====================

    /**
     * Retorna todos os dados do dashboard em JSON (para atualização via AJAX)
     */
    @GetMapping("/api/tokens/dashboard")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboard() {
        List<Token> allTokens = tokenService.getAllTokens();
        List<Token> validTokens = tokenService.getValidTokens();
        List<Token> expiredTokens = tokenService.getExpiredTokens();
        Map<String, Object> stats = tokenService.getStats();

        Map<String, Object> response = new HashMap<>();
        response.put("allTokens", mapTokenList(allTokens));
        response.put("validTokens", mapTokenList(validTokens));
        response.put("expiredTokens", mapTokenList(expiredTokens));
        response.put("stats", stats);
        response.put("serverTime", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Gera um token manualmente via API
     */
    @PostMapping("/api/tokens/generate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> generateToken() {
        Token token = tokenService.generateToken();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", mapToken(token));
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna todos os tokens
     */
    @GetMapping("/api/tokens/all")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllTokens() {
        return ResponseEntity.ok(mapTokenList(tokenService.getAllTokens()));
    }

    /**
     * Retorna tokens válidos
     */
    @GetMapping("/api/tokens/valid")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getValidTokens() {
        return ResponseEntity.ok(mapTokenList(tokenService.getValidTokens()));
    }

    /**
     * Retorna tokens inválidos
     */
    @GetMapping("/api/tokens/expired")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getExpiredTokens() {
        return ResponseEntity.ok(mapTokenList(tokenService.getExpiredTokens()));
    }

    /**
     * Retorna estatísticas
     */
    @GetMapping("/api/tokens/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(tokenService.getStats());
    }

    // ==================== Helpers ====================

    private List<Map<String, Object>> mapTokenList(List<Token> tokens) {
        return tokens.stream().map(this::mapToken).toList();
    }

    private Map<String, Object> mapToken(Token token) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", token.getId());
        map.put("tokenValue", token.getTokenValue());
        map.put("createdAt", token.getCreatedAt().toString());
        map.put("expiresAt", token.getExpiresAt().toString());
        map.put("valid", token.isValid());
        map.put("secondsRemaining", token.getSecondsRemaining());
        return map;
    }
}
