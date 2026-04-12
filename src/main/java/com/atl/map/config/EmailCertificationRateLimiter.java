package com.atl.map.config;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailCertificationRateLimiter {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate-limit:email-certification:";
    private static final DefaultRedisScript<Long> INCREMENT_WITH_TTL_SCRIPT = createIncrementWithTtlScript();

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
            Long requestCount = stringRedisTemplate.execute(
                    INCREMENT_WITH_TTL_SCRIPT,
                    List.of(rateLimitKey),
                    String.valueOf(window.toMillis())
            );

            if (requestCount == null) {
                log.warn("Rate limit 증가 실패 - clientKey: {}", clientKey);
                return false;
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

    private static DefaultRedisScript<Long> createIncrementWithTtlScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText("""
                local current = redis.call('INCR', KEYS[1])
                local ttl = redis.call('PTTL', KEYS[1])
                if ttl < 0 then
                    redis.call('PEXPIRE', KEYS[1], ARGV[1])
                end
                return current
                """);
        script.setResultType(Long.class);
        return script;
    }
}
