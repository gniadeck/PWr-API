package dev.wms.pwrapi.dao.auth;

import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.http.HttpClient;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class OauthPwrDao {

    private static final String EXPECTED_INCORRECT_LOGIN_MESSAGE = "Niepowodzenie logowania. Niepoprawna nazwa użytkownika lub hasło.";
    @Value("${url.pwr-auth}")
    private String URL_PWR_AUTH;

    public HttpClient login(String login, String password, String url, HttpClient client){
        return processLogin(login, password, url, client);
    }

    private HttpClient processLogin(String login, String password, String url, HttpClient client) {

        Document doc = client.getDocument(url);

        String oauthConsumerKey = doc.select("input[name=oauth_consumer_key]").attr("value");
        String oauthToken = doc.select("input[name=oauth_token]").attr("value");

        //prepare request for logging in
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = createRequestBody(login, password, oauthConsumerKey, oauthToken, mediaType);

        Request request = new Request.Builder()
                .url(createAuthRequestUrl(oauthConsumerKey, oauthToken))
                .method("POST", body)
                .build();

        String responseString = client.getString(request);

        if(responseString.contains(EXPECTED_INCORRECT_LOGIN_MESSAGE)){
            throw new LoginException();
        }

        return client;
    }

    @NotNull
    private String createAuthRequestUrl(String oauthConsumerKey, String oauthToken) {
        return URL_PWR_AUTH +
                "&oauth_token=" + oauthToken +
                "&oauth_consumer_key=" + oauthConsumerKey +
                "&oauth_locale=pl";
    }

    @NotNull
    private RequestBody createRequestBody(String login, String password, String oauthConsumerKey, String oauthToken, MediaType mediaType) {
        return RequestBody.create(mediaType,
                "authenticateButton=Zaloguj&ida_hf_0=&oauth_callback_url=https://jsos.pwr.edu.pl/index.php/site/loginAsStudent"
                        +
                        "&oauth_consumer_key=" + oauthConsumerKey +
                        "&oauth_locale=pl" +
                        "&oauth_request_url=http://oauth.pwr.edu.pl/oauth/authenticate&oauth_symbol=EIS" +
                        "&oauth_token=" + oauthToken +
                        "&password=" + password +
                        "&username=" + login);
    }

}
