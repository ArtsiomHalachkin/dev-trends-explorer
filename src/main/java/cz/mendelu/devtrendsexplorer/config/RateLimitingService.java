package cz.mendelu.devtrendsexplorer.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private final Bucket anonymousBucket = Bucket.builder()
            .addLimit(Bandwidth.classic(20, Refill.intervally(20, Duration.ofSeconds(60))))
            .build();

    public Bucket resolveBucket(Jwt jwt) {
        if (jwt == null) {
            return anonymousBucket;
        }

        String userId = jwt.getSubject();

        return userBuckets.computeIfAbsent(userId, this::createNewPremiumBucket);
    }

    private Bucket createNewPremiumBucket(String userId) {
        var limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofSeconds(60)));
        return Bucket.builder().addLimit(limit).build();
    }
}