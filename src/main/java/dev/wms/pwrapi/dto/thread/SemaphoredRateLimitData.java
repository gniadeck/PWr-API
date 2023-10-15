package dev.wms.pwrapi.dto.thread;

import dev.wms.pwrapi.entity.user.rateLimit.RateLimitData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

@Data
@EqualsAndHashCode(callSuper = true)
public class SemaphoredRateLimitData extends RateLimitData {

    private Semaphore semaphore;

    public SemaphoredRateLimitData(Integer requestsLeft, LocalDateTime requestUpdatedAt, Semaphore semaphore) {
        super(requestsLeft, requestUpdatedAt);
        this.semaphore = semaphore;
    }
}
