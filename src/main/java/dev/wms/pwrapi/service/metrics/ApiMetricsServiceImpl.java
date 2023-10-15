package dev.wms.pwrapi.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiMetricsServiceImpl implements ApiMetricsService {

    private final MeterRegistry meterRegistry;

    @Override
    public void onAuthenticationCorrect() {
        countAuthentication("correct");
    }

    @Override
    public void onAuthenticationFailed() {
        countAuthentication("failed");
    }

    private void countAuthentication(String result) {
        Counter.builder("api.authentications")
                .description("Counter of API Authentications per apiKey")
                .tags("result", result)
                .register(meterRegistry)
                .increment();
    }



}
