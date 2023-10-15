package dev.wms.pwrapi.service.metrics;

public interface ApiMetricsService {

    void onAuthenticationCorrect();

    void onAuthenticationFailed();
}
