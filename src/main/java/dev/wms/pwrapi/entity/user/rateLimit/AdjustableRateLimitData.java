package dev.wms.pwrapi.entity.user.rateLimit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdjustableRateLimitData extends RateLimitData {

    protected Integer requestLimit;
    protected Integer newRequestsPerInterval;

    public AdjustableRateLimitData(
            Integer requestsLeft,
            LocalDateTime requestUpdatedAt,
            Integer requestLimit,
            Integer newRequestsPerInterval) {

        super(requestsLeft, requestUpdatedAt);
        this.requestLimit = requestLimit;
        this.newRequestsPerInterval = newRequestsPerInterval;
    }
}
