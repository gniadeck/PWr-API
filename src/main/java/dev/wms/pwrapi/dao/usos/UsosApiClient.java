package dev.wms.pwrapi.dao.usos;

import dev.wms.pwrapi.utils.http.HttpClient;

import java.util.Map;

public interface UsosApiClient {
    String perform(HttpClient client, String method, Map<String, String> additionalProperties);
}
