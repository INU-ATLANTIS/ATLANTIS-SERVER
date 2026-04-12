package com.atl.map.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailCertificationRateLimiter {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate-limit:email-certification:";

    private final int maxRequests;
    private final Duration window;
    private final StringRedisTemplate stringRedisTemplate;

    public EmailCertificationRateLimiter(
            StringRedisTemplate stringRedisTemplate,
            @Value("${app.security.rate-limit.email-certification.max-requests}") int maxRequests,
            @Value("${app.security.rate-limit.email-certification.window-seconds}") long windowSeconds
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.maxRequests = maxRequests;
        this.window = Duration.ofSeconds(windowSeconds);
    }

    public boolean isAllowed(HttpServletRequest request) {
        String clientKey = extractClientKey(request);
        String rateLimitKey = RATE_LIMIT_KEY_PREFIX + clientKey;

        try {
            Long requestCount = stringRedisTemplate.opsForValue().increment(rateLimitKey);

            if (requestCount == null) {
                log.warn("Rate limit 증가 실패 - clientKey: {}", clientKey);
                return false;
            }

            if (requestCount == 1L) {
                stringRedisTemplate.expire(rateLimitKey, window);
            }

            if (requestCount > maxRequests) {
                log.warn("이메일 인증 요청 제한 초과 - clientKey: {}, count: {}", clientKey, requestCount);
                return false;
            }

            return true;
        } catch (Exception exception) {
            log.error("Rate limit 확인 실패 - clientKey: {}", clientKey, exception);
            return true;
        }
    }

    private String extractClientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }

        return request.getRemoteAddr();
    }
}
