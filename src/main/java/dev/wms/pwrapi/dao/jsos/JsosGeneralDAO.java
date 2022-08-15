package dev.wms.pwrapi.dao.jsos;

import dev.wms.pwrapi.dto.jsos.JsosConnection;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;

import java.io.IOException;

public interface JsosGeneralDAO {
    /**
     * Login's to JSOS using Cookies. Method is using custom OkHttp's CookieJar implementation, which
     * swaps cookies for session cookies if needed.
     * @param login
     * @param password
     * @return
     * @throws IOException
     * @throws LoginException
     */
    JsosConnection login(String login, String password) throws IOException, LoginException;

    /**
     * Return's client instance. Mostly used for logging in in JsosHttpUtils class and getting logged client instance
     * @return OkHttpClient instance
     */
    okhttp3.OkHttpClient getClient();
}
