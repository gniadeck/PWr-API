package dev.wms.pwrapi.utils.jsos;

import dev.wms.pwrapi.dao.jsos.JsosGeneralDAOImpl;
import dev.wms.pwrapi.dto.jsos.JsosConnection;
import okhttp3.OkHttpClient;

import java.io.IOException;

public class JsosHttpUtils {

    /**
     * Returns logged client instance using JsosGeneralDao
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @return Logged instance of OkHttpClient (on JSOS's main page)
     * @throws IOException If parsing goes wrong
     */
    public static OkHttpClient getLoggedClient(String login, String password) throws IOException {

        JsosGeneralDAOImpl general = new JsosGeneralDAOImpl();
        JsosConnection jsosConnection = general.login(login, password);
        return general.getClient();

    }



}
