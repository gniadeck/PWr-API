package dev.wms.pwrapi.service.rateLimit;

import dev.wms.pwrapi.entity.user.rateLimit.RateLimitData;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Semaphore;

/**
 * @param <T> object based on which will be judged if the access is allowed.
 * @param <U> object containing rate-limiting data.
 * */
public abstract class RateLimiter<T, U extends RateLimitData> {

    @Value("${rate-limiting.update-interval-in-secs}")
    protected int REFRESH_INTERVAL;

    /**
     * @param data object based on which will be judged if the access is allowed.
     * @return true if the access is allowed, false otherwise.
     * */
    public abstract boolean tryAccess(T data);

    protected boolean handleRateLimiting(T rateLimitedEntity, U rateLimitData, Semaphore semaphore) {
        semaphore.acquireUninterruptibly();
        boolean returnVal = false;

        try{
            beforeRateLimiting(rateLimitedEntity, rateLimitData);

            if (rateLimitData.getRequestsLeft() != 0) {
                rateLimitData.setRequestsLeft(rateLimitData.getRequestsLeft() - 1);
                onSuccess(rateLimitedEntity, rateLimitData);
                returnVal = true;
            }

        } finally {
            semaphore.release();
        }
        return returnVal;
    }

    /**
     * A method to prepare the data before checking rate-limiting conditions.
     * */
    protected void beforeRateLimiting(T data){}

    /**
     * A method to prepare the data before checking rate-limiting conditions.
     * */
    protected void beforeRateLimiting(U data){}

    private void beforeRateLimiting(T rateLimitedEntity, U rateLimitData) {
        beforeRateLimiting(rateLimitedEntity);
        beforeRateLimiting(rateLimitData);
    }

    /**
     * A method to do some operations on data after successful rate-limiting check.
     * */
    protected void onSuccess(T data){}

    /**
     * A method to do some operations on data after successful rate-limiting check.
     * */
    protected void onSuccess(U data){}

    private void onSuccess(T rateLimitedEntity, U rateLimitData) {
        onSuccess(rateLimitedEntity);
        onSuccess(rateLimitData);
    }

    protected void updateRateLimitData(
            RateLimitData rateLimitData,
            int additionalRequestsPerInterval,
            int maxRateLimit) {

        long secondsSinceLastRequest = rateLimitData
                .getRequestsLeftUpdatedAt()
                .until(LocalDateTime.now(), ChronoUnit.SECONDS);

        int requestsToAdd = (int) (additionalRequestsPerInterval * (secondsSinceLastRequest / REFRESH_INTERVAL));
        int totalRequests = rateLimitData.getRequestsLeft() + requestsToAdd;

        if (requestsToAdd > 0) {
            LocalDateTime updatedTimestamp = LocalDateTime.now().minusSeconds(
                    secondsSinceLastRequest % REFRESH_INTERVAL
            );
            rateLimitData.setRequestsLeftUpdatedAt(updatedTimestamp);
        }

        rateLimitData.setRequestsLeft(Math.min(maxRateLimit, totalRequests));
    }
}