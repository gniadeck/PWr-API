package dev.wms.pwrapi.scrapper.edukacja;

import dev.wms.pwrapi.dto.edukacja.EduConnection;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import io.github.bonigarcia.wdm.WebDriverManager;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.List;

public class EduScrapperServices {

    /** Selenium login **/
    public static WebDriver login(String login, String password) {
        WebDriver driver = null;

        WebDriverManager.chromedriver().setup();

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
        driver = new ChromeDriver(options);


        driver.get("https://edukacja.pwr.wroc.pl/");

        driver.findElement(By.name("login")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(password);

        driver.findElement(By.cssSelector(".BUTTON_ZALOGUJ")).click();


        return driver;
    }

    //** logins to edukacja.cl using HTML methods. Throws error if something is wrong */
    public static EduConnection fetchHTMLConnectionDetails(String login, String password) throws IOException, LoginException {

        // the object where connection details are stored
        EduConnection connectionDetails = new EduConnection();

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
        connectionDetails.setJsessionid(jsessionid);
        System.out.println(Cookielist);

        // retrieves login page of education as string
        String responseBody = response.body().string();

        // parses string response body with Jsoup parser
        Document page = Jsoup.parse(responseBody);

        String webTOKEN = page.select("[name=cl.edu.web.TOKEN]").attr("value");
        connectionDetails.setWebToken(webTOKEN);


        System.out.println("Web token: " + webTOKEN);
        System.out.println(page.html());

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "cl.edu.web.TOKEN=" + webTOKEN + "&login=" + login + "&password=" + password);
        request = new Request.Builder()
                .url("https://edukacja.pwr.wroc.pl/EdukacjaWeb/logInUser.do")
                .method("POST", body)
                .addHeader("Cookie", "JSESSIONID=" + jsessionid)
                .build();

        response = client.newCall(request).execute();

        if(response.header("Content-Location").contains("logInError.jsp")){
            System.out.println("Throwing login exception");
            throw new LoginException();
        }

        responseBody = response.body().string();
        page = Jsoup.parse(responseBody);
        String sessionTOKEN = page.select("[name=clEduWebSESSIONTOKEN]").attr("value");

        connectionDetails.setSessionToken(sessionTOKEN);

        return connectionDetails;
    }

}
