package dev.wms.pwrapi.entity.user.rateLimit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitData {
    protected Integer requestsLeft;
    protected LocalDateTime requestsLeftUpdatedAt;
}