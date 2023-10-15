package dev.wms.pwrapi.dao.auth;

import dev.wms.pwrapi.utils.cookies.CookieJarImpl;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class UsosAuthDao implements AuthDao {

    private final OauthUsosDao oauthUsosDao;
    @Value("${url.usos}")
    private String URL_USOS;
    @Value("${url.usos-login-site}")
    private String URL_USOS_LOGIN_SITE;

    @Value("${usos.client-timeout-in-seconds}")
    private int timeoutInSeconds;

    @Override
    @Cacheable("usos-login")
    public HttpClient login(String login, String password) {

        Duration timeoutDuration = Duration.ofSeconds(timeoutInSeconds);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .cookieJar(new CookieJarImpl())
                .callTimeout(timeoutDuration)
                .readTimeout(timeoutDuration)
                .connectTimeout(timeoutDuration)
                .writeTimeout(timeoutDuration)
                .build();

        var httpClient = new HttpClient(client);
        httpClient.getResponse(URL_USOS);
        return oauthUsosDao.login(login, password, URL_USOS_LOGIN_SITE, httpClient);
    }
}
