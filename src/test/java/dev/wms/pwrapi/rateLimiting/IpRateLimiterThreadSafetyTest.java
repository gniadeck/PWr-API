package dev.wms.pwrapi.rateLimiting;

import dev.wms.pwrapi.dto.thread.SemaphoredRateLimitData;
import dev.wms.pwrapi.service.rateLimit.IpInMemoryRateLimiter;
import dev.wms.pwrapi.utils.map.ExpirationCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IpRateLimiterThreadSafetyTest {

    @Mock
    private IpInMemoryRateLimiter rateLimiter;

    private final int MAX_RATE_LIMIT_PER_CLIENT = 10_000;
    private final int NUM_THREADS = 20;
    private ExpirationCache<String, SemaphoredRateLimitData> cache;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setup() {
        rateLimiter = new IpInMemoryRateLimiter();
        ReflectionTestUtils.setField(rateLimiter, "MAX_RATE_LIMIT_PER_CLIENT", MAX_RATE_LIMIT_PER_CLIENT);
        ReflectionTestUtils.setField(rateLimiter, "REFRESH_INTERVAL", 9999);
        ReflectionTestUtils.setField(rateLimiter, "ADD_COUNT_PER_INTERVAL", 0);

        this.cache =
                (ExpirationCache<String, SemaphoredRateLimitData>)
                        ReflectionTestUtils.getField(rateLimiter, "rateLimitDataCache");

        ReflectionTestUtils.setField(cache, "cacheTTL", 9999);
    }

    @Test
    public void tryAccessTest_multipleThreads_oneIp() throws InterruptedException {
        String ip = "127.0.0.1";
        int numIterations = MAX_RATE_LIMIT_PER_CLIENT/NUM_THREADS;

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    for (int j = 0; j < numIterations; j++) {
                        assertTrue(rateLimiter.tryAccess(ip));
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        assertEquals(0, cache.get(ip).getRequestsLeft());
        assertFalse(rateLimiter.tryAccess(ip));
    }

    @Test
    public void tryAccessTest_multipleThreads_multipleIps() throws InterruptedException {
        int numIterations = MAX_RATE_LIMIT_PER_CLIENT/NUM_THREADS;

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadIndex = i;
            executorService.execute(() -> {
                try {
                    String ip = "127.0.0." + (threadIndex + 1);
                    for (int j = 0; j < numIterations; j++) {
                        assertTrue(rateLimiter.tryAccess(ip));
                    }
                    assertFalse(rateLimiter.tryAccess(ip));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }
}
