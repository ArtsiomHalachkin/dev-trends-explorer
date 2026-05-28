package cz.mendelu.devtrendsexplorer.config;

import cz.mendelu.devtrendsexplorer.utils.exceptions.RateLimitException;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingAspect {

    private final RateLimitingService rateLimitingService;

    @Before("@annotation(RateLimited) || @within(RateLimited)")
    public void enforceRateLimit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = null;

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            jwt = (Jwt) authentication.getPrincipal();
        }

        Bucket bucket = rateLimitingService.resolveBucket(jwt);

        if (!bucket.tryConsume(1)) {
            String user = (jwt != null) ? jwt.getSubject() : "Anonymous";
            log.warn("Rate limit exceeded for user: {}", user);
            throw new RateLimitException();
        }
    }
}