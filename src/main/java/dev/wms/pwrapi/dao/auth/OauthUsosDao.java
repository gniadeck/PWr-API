package dev.wms.pwrapi.dao.auth;

import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.http.HttpClient;
import okhttp3.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class OauthUsosDao {

    private static final String EXPECTED_INCORRECT_LOGIN_MESSAGE = "Nieprawidłowa nazwa użytkownika lub hasło.";

    @Value("${url.usos-login-auth}")
    private String URL_USOS_LOGIN_AUTH;

    public HttpClient login(String login, String password, String loginSiteUrl, HttpClient client){
        return processLogin(login, password, loginSiteUrl, client);
    }

    private HttpClient processLogin(String login, String password, String loginSiteUrl, HttpClient client) {

        Document document = client.getDocument(loginSiteUrl);
        String authLink = document.getElementsByClass("login-form").attr("action");

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "password=" + password + "&username=" + login);
        Request request = new Request.Builder()
                .url(authLink)
                .method("POST", body)
                .build();

        document = client.getDocument(request);

        Elements alerts = document.getElementsByClass("alert alert-error");
        checkForAuthErrors(alerts);

        return client;
    }

    private void checkForAuthErrors(Elements alerts){
        for(Element alert: alerts){
            if(EXPECTED_INCORRECT_LOGIN_MESSAGE
                    .equals(Objects.requireNonNull(alert.getElementsByClass("feedback").first()).text())){
                throw new LoginException();
            }
        }
    }

}
