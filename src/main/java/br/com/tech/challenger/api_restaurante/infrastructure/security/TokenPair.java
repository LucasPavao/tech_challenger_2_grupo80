package br.com.tech.challenger.api_restaurante.infrastructure.security;

import java.time.Instant;

public record TokenPair(String accessToken, String refreshToken, Instant expiresAt) {
}
