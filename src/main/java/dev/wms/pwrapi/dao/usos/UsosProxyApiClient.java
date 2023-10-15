package dev.wms.pwrapi.dao.usos;

import dev.wms.pwrapi.utils.http.HttpClient;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UsosProxyApiClient implements UsosApiClient {

    @Value("${usos.proxy.url}")
    private String proxyUrl;
    private Pattern csrfPattern = Pattern.compile("csrftoken = \"\\S*\"");

    @Override
    public String perform(HttpClient client, String method, Map<String, String> additionalProperties) {

        Map<String, String> params = new HashMap<>(additionalProperties);
        params.put("_csrftoken_", getCsrfToken(client));
        RequestBody body = formBodyFromMap(params);
        Request request = new Request.Builder()
                .url(proxyUrl + "?_method_=" + method)
                .method("POST", body)
                .build();


        return client.getString(request);
    }

    private FormBody formBodyFromMap(Map<String, String> params){
        var builder = new FormBody.Builder();
        params.entrySet().stream()
                .forEach(entry -> builder.add(entry.getKey(), entry.getValue()));
        return builder.build();
    }

    private String getCsrfToken(HttpClient client){
        var response = client.getString("https://web.usos.pwr.edu.pl/kontroler.php?_action=dodatki/index");
        var matcher = csrfPattern.matcher(response);
        matcher.find();
        return clearCsrfTokenMatch(matcher.group());
    }

    private String clearCsrfTokenMatch(String token){
        return token.replace("csrftoken = ", "").replace("\"", "");
    }
}
