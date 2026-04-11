package com.atl.map.config;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class EmailCertificationRateLimiter {

    private final int maxRequests;
    private final Duration window;
    private final Map<String, Deque<Instant>> requestsByClient = new ConcurrentHashMap<>();

    public EmailCertificationRateLimiter(
            @Value("${app.security.rate-limit.email-certification.max-requests}") int maxRequests,
            @Value("${app.security.rate-limit.email-certification.window-seconds}") long windowSeconds
    ) {
        this.maxRequests = maxRequests;
        this.window = Duration.ofSeconds(windowSeconds);
    }

    public boolean isAllowed(HttpServletRequest request) {
        String clientKey = extractClientKey(request);
        Instant now = Instant.now();
        Deque<Instant> requestTimes = requestsByClient.computeIfAbsent(clientKey, key -> new ArrayDeque<>());

        synchronized (requestTimes) {
            Instant threshold = now.minus(window);
            while (!requestTimes.isEmpty() && requestTimes.peekFirst().isBefore(threshold)) {
                requestTimes.pollFirst();
            }

            if (requestTimes.size() >= maxRequests) {
                return false;
            }

            requestTimes.addLast(now);
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
