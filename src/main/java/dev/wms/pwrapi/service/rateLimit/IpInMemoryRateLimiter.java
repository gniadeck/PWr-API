package dev.wms.pwrapi.service.rateLimit;

import dev.wms.pwrapi.dto.thread.SemaphoredRateLimitData;
import dev.wms.pwrapi.utils.map.ExpirationCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

@Service
public class IpInMemoryRateLimiter extends RateLimiter<String, SemaphoredRateLimitData> {

    @Value("${rate-limiting.max-requests.unregistered}")
    private int MAX_RATE_LIMIT_PER_CLIENT;

    @Value("${rate-limiting.add-requests-per-interval.unregistered}")
    private int ADD_COUNT_PER_INTERVAL;

    private final ExpirationCache<String, SemaphoredRateLimitData> rateLimitDataCache;

    public IpInMemoryRateLimiter() {
        this.rateLimitDataCache = new ExpirationCache<>();
    }

    @Override
    public boolean tryAccess(String ip) {
        SemaphoredRateLimitData data = rateLimitDataCache.computeIfAbsent(ip, id ->
                new SemaphoredRateLimitData(
                        MAX_RATE_LIMIT_PER_CLIENT,
                        LocalDateTime.now(),
                        new Semaphore(1)
                ));

        return handleRateLimiting(ip, data, data.getSemaphore());
    }

    @Override
    protected void beforeRateLimiting(SemaphoredRateLimitData rateLimitData) {
        updateRateLimitData(rateLimitData, ADD_COUNT_PER_INTERVAL, MAX_RATE_LIMIT_PER_CLIENT);
    }
}
