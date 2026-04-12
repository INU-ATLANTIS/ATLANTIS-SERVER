package com.atl.map.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import jakarta.servlet.http.HttpServletRequest;

class EmailCertificationRateLimiterTest {

    @Test
    @DisplayName("요청 횟수가 제한 이하이면 허용한다")
    void allowsWhenRequestCountIsWithinLimit() {
        StringRedisTemplate stringRedisTemplate = Mockito.mock(StringRedisTemplate.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        EmailCertificationRateLimiter rateLimiter =
                new EmailCertificationRateLimiter(stringRedisTemplate, 3, 300);

        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(stringRedisTemplate.execute(
                any(RedisScript.class),
                eq(List.of("rate-limit:email-certification:127.0.0.1")),
                eq("300000")))
                .thenReturn(2L);

        boolean allowed = rateLimiter.isAllowed(request);

        assertTrue(allowed);
        verify(stringRedisTemplate).execute(
                any(RedisScript.class),
                eq(List.of("rate-limit:email-certification:127.0.0.1")),
                eq("300000"));
    }

    @Test
    @DisplayName("요청 횟수가 제한을 초과하면 차단한다")
    void blocksWhenRequestCountExceedsLimit() {
        StringRedisTemplate stringRedisTemplate = Mockito.mock(StringRedisTemplate.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        EmailCertificationRateLimiter rateLimiter =
                new EmailCertificationRateLimiter(stringRedisTemplate, 3, 300);

        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.10");
        when(stringRedisTemplate.execute(
                any(RedisScript.class),
                eq(List.of("rate-limit:email-certification:203.0.113.10")),
                eq("300000")))
                .thenReturn(4L);

        boolean allowed = rateLimiter.isAllowed(request);

        assertFalse(allowed);
    }
}
