package dev.wms.pwrapi.dao.auth;

import dev.wms.pwrapi.utils.cookies.CookieJarImpl;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import okhttp3.OkHttpClient;

@Repository
@RequiredArgsConstructor
public class JsosAuthDao implements AuthDao {

    private final OauthPwrDao oauthPwrDao;
    @Value("${url.jsos}")
    private String URL_JSOS;
    @Value("${url.login-as-student}")
    private String URL_LOGIN_AS_STUDENT;

    @Override
    public HttpClient login(String login, String password) {
        var client = new HttpClient();
        client.getResponse(URL_JSOS);
        return oauthPwrDao.login(login, password, URL_LOGIN_AS_STUDENT, client);
    }

}
