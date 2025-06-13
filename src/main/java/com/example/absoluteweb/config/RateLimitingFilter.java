package com.example.absoluteweb.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private static final long MIN_INTERVAL = 1000L;
    private final ConcurrentHashMap<String, Long> lastRequestTimeMap = new ConcurrentHashMap<>();

    private static final List<String> WHITELIST_PATTERNS = List.of(
            "/api/site/accounts/check-register",
            "/api/site/accounts/send-verification",
            "/api/site/accounts/verify-code",
            "/api/files/**",
            "/api/forum/user/create-user",
            "/api/forum/user/check-register",
            "/api/forum/user/send-verification",
            "/api/forum/user/verify-code",
            "/api/forum/user/login",
            "/api/forum/user/get-user/{id}",
            "/api/forum/user/check-user-token"
    );

    // Інстанс AntPathMatcher для перевірки співпадіння з шаблонами
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Перевіряємо, чи співпадає URI запиту з одним із шаблонів whitelist
        boolean isWhitelisted = WHITELIST_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));

        if (isWhitelisted) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientKey = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = lastRequestTimeMap.get(clientKey);
        if (lastRequestTime != null && (currentTime - lastRequestTime < MIN_INTERVAL)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests, please wait 1 seconds.");
            return;
        }
        lastRequestTimeMap.put(clientKey, currentTime);
        filterChain.doFilter(request, response);
    }
}
