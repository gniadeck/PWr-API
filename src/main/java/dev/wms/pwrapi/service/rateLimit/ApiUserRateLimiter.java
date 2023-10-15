package dev.wms.pwrapi.service.rateLimit;

import dev.wms.pwrapi.dao.user.ApiUserRepository;
import dev.wms.pwrapi.entity.user.ApiUser;
import dev.wms.pwrapi.entity.user.rateLimit.AdjustableRateLimitData;
import dev.wms.pwrapi.utils.map.ExpirationCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Semaphore;

@Service
public class ApiUserRateLimiter extends RateLimiter<ApiUser, AdjustableRateLimitData> {

    private final ApiUserRepository userRepository;
    private final ExpirationCache<Long, Semaphore> semaphoreCache;

    public ApiUserRateLimiter(ApiUserRepository userRepository) {
        this.userRepository = userRepository;
        this.semaphoreCache = new ExpirationCache<>();
    }

    @Override
    @Transactional
    public boolean tryAccess(ApiUser user) {
        Semaphore semaphore = semaphoreCache.computeIfAbsent(user.getId(), id -> new Semaphore(1));
        return handleRateLimiting(user, user.getRateLimitData(), semaphore);
    }

    @Override
    protected void beforeRateLimiting(AdjustableRateLimitData rateLimitData) {
        updateRateLimitData(rateLimitData, rateLimitData.getNewRequestsPerInterval(), rateLimitData.getRequestLimit());
    }

    @Override
    protected void onSuccess(ApiUser user) {
        AdjustableRateLimitData rateLimitData = user.getRateLimitData();

        userRepository.updateRequestDataById(
                user.getId(),
                rateLimitData.getRequestsLeft(),
                rateLimitData.getRequestsLeftUpdatedAt()
        );
    }
}