package dev.wms.pwrapi.scrapper.eportal;

import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dev.wms.pwrapi.dto.eportal.userDetails;
import dev.wms.pwrapi.utils.cookies.CookieJarImpl;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import lombok.Getter;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Getter
public class EportalScrapperService {

    private static OkHttpClient client;

    public static userDetails loginToEportal(String login, String password) throws IOException {


        CookieJar jar = new CookieJarImpl();
        client = new OkHttpClient.Builder()
            .cookieJar(jar)
            .build();

        Request request = new Request.Builder()
            .url("https://eportal.pwr.edu.pl/login/index.php?authJSOS=JSOS")
            .build();
        
        Response response = client.newCall(request).execute();

     String responseString = response.body().string();
        System.out.println(responseString);
        Document doc = Jsoup.parse(responseString);

        String oauthConsumerKey = doc.select("input[name=oauth_consumer_key]").attr("value");
        String oauthToken = doc.select("input[name=oauth_token]").attr("value");

    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
     RequestBody body = RequestBody.create(mediaType,
                "authenticateButton=Zaloguj&ida_hf_0=&oauth_callback_url=https://jsos.pwr.edu.pl/index.php/site/loginAsStudent"
                        +
                        "&oauth_consumer_key=" + oauthConsumerKey +
                        "&oauth_locale=pl" +
                        "&oauth_request_url=http://oauth.pwr.edu.pl/oauth/authenticate&oauth_symbol=EIS" +
                        "&oauth_token=" + oauthToken +
                        "&password=" + password +
                        "&username=" + login);

            request = new Request.Builder()
                .url("https://oauth.pwr.edu.pl/oauth/authenticate?9-1.IFormSubmitListener-authenticateForm" +
                        "&oauth_token=" + oauthToken +
                        "&oauth_consumer_key=" + oauthConsumerKey +
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
                // .addHeader("Cookie", "JSESSIONID=" + jsosConnection.getOauthSessionToken())
                .build();

        response = client.newCall(request).execute();
        responseString = response.body().string();
        if(responseString.contains("Niepowodzenie logowania. Niepoprawna nazwa użytkownika lub hasło.")) throw new LoginException();

        doc = Jsoup.parse(responseString);

        userDetails details = new userDetails().builder()
            .username("Feautre not supported.")
            .userID(0)
            .build();

        return details;
    }

    public static OkHttpClient getClient() {
        return client;
    }

}
