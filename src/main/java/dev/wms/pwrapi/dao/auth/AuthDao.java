package dev.wms.pwrapi.dao.auth;

import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.http.HttpClient;

import java.io.IOException;


public interface AuthDao {
    /**
     * Login's to PWr websites using Cookies. Method is using custom OkHttp's CookieJar implementation, which
     * swaps cookies for session cookies if needed.
     *
     * @param login
     * @param password
     * @return
     * @throws IOException
     * @throws LoginException
     */
    HttpClient login(String login, String password);

}
