package dev.wms.pwrapi.scrapper.edukacja;

import dev.wms.pwrapi.dto.edukacja.EdukacjaConnection;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import io.github.bonigarcia.wdm.WebDriverManager;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EdukacjaScrapperService {

    public WebDriver login(String login, String password) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = getDriverOptions();
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://edukacja.pwr.wroc.pl/");

        driver.findElement(By.name("login")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(password);

        driver.findElement(By.cssSelector(".BUTTON_ZALOGUJ")).click();


        return driver;
    }

    @NotNull
    private ChromeOptions getDriverOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.addArguments("--headless");
        options.addArguments("--blink-settings=imagesEnabled=false");
        return options;
    }


    public EdukacjaConnection fetchHTMLConnectionDetails(String login, String password) throws IOException, LoginException {

        OkHttpClient client = new OkHttpClient();

        // connects with education website, receives cookies and parses jsessionid
        // parsed jsessionid is then stored in connectionDetails DTO
        Request request = new Request.Builder()
                .url("https://edukacja.pwr.wroc.pl/EdukacjaWeb/studia.do")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        List<String> Cookielist = response.headers().values("Set-Cookie");
        String jsessionid = (Cookielist .get(0).split(";"))[0].replace("JSESSIONID=", "");

        String responseBody = response.body().string();

        Document page = Jsoup.parse(responseBody);

        String webTOKEN = page.select("[name=cl.edu.web.TOKEN]").attr("value");

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "cl.edu.web.TOKEN=" + webTOKEN + "&login=" + login + "&password=" + password);
        request = new Request.Builder()
                .url("https://edukacja.pwr.wroc.pl/EdukacjaWeb/logInUser.do")
                .method("POST", body)
                .addHeader("Cookie", "JSESSIONID=" + jsessionid)
                .build();

        response = client.newCall(request).execute();

        assertNoError(response);

        responseBody = response.body().string();
        page = Jsoup.parse(responseBody);
        String sessionTOKEN = page.select("[name=clEduWebSESSIONTOKEN]").attr("value");

        return new EdukacjaConnection(sessionTOKEN, webTOKEN, jsessionid);
    }

    private void assertNoError(Response response) {
        if(response.header("Content-Location").contains("logInError.jsp")){
            throw new LoginException();
        }
    }

}
