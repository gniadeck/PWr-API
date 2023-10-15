package dev.wms.pwrapi.dao.auth;


import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EportalAuthDao implements AuthDao {

    private final OauthPwrDao oauthPwrDao;
    @Value("${url.auth-by-jsos}")
    private String URL_EPORTAL_AUTH;

    @Override
    public HttpClient login(String login, String password) {
        return oauthPwrDao.login(login, password, URL_EPORTAL_AUTH, new HttpClient());
    }

}
