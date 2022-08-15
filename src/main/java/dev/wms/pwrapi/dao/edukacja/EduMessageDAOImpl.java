package dev.wms.pwrapi.dao.edukacja;

import dev.wms.pwrapi.scrapper.edukacja.EduScrapperServices;
import dev.wms.pwrapi.dto.edukacja.EduConnection;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class EduMessageDAOImpl {

    public static void fetchMessage(String login, String password) throws IOException, LoginException {

        EduConnection eduConnection = EduScrapperServices.fetchHTMLConnectionDetails(login, password);
        //eduConnection.

        String URL = "https://edukacja.pwr.wroc.pl/EdukacjaWeb/podgladWiadomosci.do?clEduWebSESSIONTOKEN=" + eduConnection.getSessionToken() + "==&event=positionRow&rowId=75792517&positionIterator=WiadomoscWSkrzynceViewIterator/WEB-INF/pages/secure/teksty/tekst.jsp";
        //String URLIndeks = "https://edukacja.pwr.wroc.pl/EdukacjaWeb/indeks.do?clEduWebSESSIONTOKEN=" + eduConnection.getSessionToken() + "==&event=WyborSluchacza";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(URL)
                .method("GET", null)
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("host", "edukacja.pwr.wroc.pl")
                .addHeader("Cookie", "JSESSIONID=" + eduConnection.getJsessionid())
                .build();
        Response response = client.newCall(request).execute();

        String responseStr = response.body().string();
        //System.out.println(responseStr);

        Document document = Jsoup.parse(responseStr);
        //String message = document.selectXpath("//*[@id=\"GORAPORTALU\"]/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[1]/tbody/tr[8]/td[2]").text();
        //System.out.println(message);
        //*[@id="GORAPORTALU"]/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[1]/tbody/tr[8]/td[2]/text()[1]
        //System.out.println(document.selectXpath("/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[4]"));

    }

}
