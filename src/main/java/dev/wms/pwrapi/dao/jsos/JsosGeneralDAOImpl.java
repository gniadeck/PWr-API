package dev.wms.pwrapi.dao.jsos;

import java.io.IOException;
import java.util.List;

import dev.wms.pwrapi.utils.http.HttpUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.dto.jsos.JsosConnection;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.jsos.cookies.CookieJarImpl;
import lombok.Getter;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Repository
@Getter
public class JsosGeneralDAOImpl implements JsosGeneralDAO {

    private OkHttpClient client;


    @Override
    public JsosConnection login(String login, String password) throws IOException, LoginException {

        CookieJar cookieJar = new CookieJarImpl();
        JsosConnection jsosConnection = new JsosConnection();

        client = new OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .build();

        Request request = new Request.Builder()
                .url("https://jsos.pwr.edu.pl/")
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();
        List<String> Cookielist = response.headers().values("Set-Cookie");

        String jsessionid = (Cookielist.get(0).split(";"))[0].replace("JSOSSESSID=", "");
        String YII_CSRF_TOKEN = (Cookielist.get(1).split(";"))[0].replace("YII_CSRF_TOKEN=", "");


        jsosConnection.setSessionID(jsessionid);
        jsosConnection.setYII_CSRF_TOKEN(YII_CSRF_TOKEN);

        // get oauth details
        Document doc = HttpUtils.makeRequestWithClientAndGetDocument(client,
                "https://jsos.pwr.edu.pl/index.php/site/loginAsStudent");

        String oauthConsumerKey = doc.select("input[name=oauth_consumer_key]").attr("value");
        String oauthToken = doc.select("input[name=oauth_token]").attr("value");

        jsosConnection.setOauthConsumerKey(oauthConsumerKey);
        jsosConnection.setOauthToken(oauthToken);

        //prepare request for logging in
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType,
                "authenticateButton=Zaloguj&ida_hf_0=&oauth_callback_url=https://jsos.pwr.edu.pl/index.php/site/loginAsStudent"
                        +
                        "&oauth_consumer_key=" + jsosConnection.getOauthConsumerKey() +
                        "&oauth_locale=pl" +
                        "&oauth_request_url=http://oauth.pwr.edu.pl/oauth/authenticate&oauth_symbol=EIS" +
                        "&oauth_token=" + jsosConnection.getOauthToken() +
                        "&password=" + password +
                        "&username=" + login);

        request = new Request.Builder()
                .url("https://oauth.pwr.edu.pl/oauth/authenticate?9-1.IFormSubmitListener-authenticateForm" +
                        "&oauth_token=" + jsosConnection.getOauthToken() +
                        "&oauth_consumer_key=" + jsosConnection.getOauthConsumerKey() +
                        "&oauth_locale=pl")
                .method("POST", body)
                .addHeader("sec-ch-ua",
                        "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36")
                .addHeader("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Sec-Fetch-Dest", "document")
                .build();
        
        response = client.newCall(request).execute();

        String responseString = response.body().string();
        response.body().close();

        if(responseString.contains("Niepowodzenie logowania. Niepoprawna nazwa użytkownika lub hasło.")){
            throw new LoginException();
        }

        jsosConnection.setOauthSessionToken(((CookieJarImpl) cookieJar).getCookieStore()
                .get("oauth.pwr.edu.pl").toString().split(";")[0].replace("JSESSIONID=", ""));

        jsosConnection.setSessionID(((CookieJarImpl) cookieJar).getCookieStore()
                .get("jsos.pwr.edu.pl").get(2).toString().split(";")[0].replace("JSOSSESSID=", ""));

        return jsosConnection;
    }

}
