package com.example.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secret = "t6vfi5rASu42reku4ZDzJPtidoOn6g4WBAzk5CYr0s6GOdQiBJcQ//3XqHI0Kfd/DHb8kGRSRlaK+M0H4b8ymw==";
    private final long expiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", expiration);
    }

    @Test
    void shouldGenerateAndValidateTokenSuccessfully() {
        UserDetails userDetails = User.withUsername("john@example.com")
                .password("password123")
                .authorities(Collections.emptyList())
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals("john@example.com", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertFalse(jwtService.isTokenExpired(token));
        assertTrue(jwtService.extractExpiration(token).after(new Date()));
    }

    @Test
    void shouldGenerateTokenWithExtraClaims() {
        UserDetails userDetails = User.withUsername("alice@example.com")
                .password("secret")
                .authorities(Collections.emptyList())
                .build();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_ADMIN");
        claims.put("name", "Alice Admin");

        String token = jwtService.generateToken(claims, userDetails);

        assertEquals("alice@example.com", jwtService.extractUsername(token));
        assertEquals("ROLE_ADMIN", jwtService.extractClaim(token, c -> c.get("role", String.class)));
        assertEquals("Alice Admin", jwtService.extractClaim(token, c -> c.get("name", String.class)));
    }

    @Test
    void shouldFailValidationForDifferentUser() {
        UserDetails userDetails1 = User.withUsername("user1@example.com")
                .password("pass")
                .authorities(Collections.emptyList())
                .build();

        UserDetails userDetails2 = User.withUsername("user2@example.com")
                .password("pass")
                .authorities(Collections.emptyList())
                .build();

        String token = jwtService.generateToken(userDetails1);

        assertFalse(jwtService.isTokenValid(token, userDetails2));
    }
}
