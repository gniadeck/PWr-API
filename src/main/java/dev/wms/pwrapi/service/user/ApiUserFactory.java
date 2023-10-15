package dev.wms.pwrapi.service.user;

import dev.wms.pwrapi.entity.user.ApiUser;
import dev.wms.pwrapi.entity.user.rateLimit.AdjustableRateLimitData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApiUserFactory {

    @Value("${rate-limiting.default-max-requests.registered}")
    private Integer DEFAULT_MAX_REQUESTS;

    @Value("${rate-limiting.default-add-requests-per-interval.registered}")
    private Integer DEFAULT_NEW_REQUESTS_PER_INTERVAL;

    public ApiUser createNewUser(String email){
        AdjustableRateLimitData rateLimitData = new AdjustableRateLimitData(
                0,
                LocalDateTime.now(),
                DEFAULT_MAX_REQUESTS,
                DEFAULT_NEW_REQUESTS_PER_INTERVAL
        );

        return ApiUser.builder()
                .email(email)
                .enabled(true)
                .rateLimitData(rateLimitData)
                .build();
    }
}
