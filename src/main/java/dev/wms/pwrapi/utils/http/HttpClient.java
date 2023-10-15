package dev.wms.pwrapi.utils.http;

import dev.wms.pwrapi.utils.cookies.CookieJarImpl;
import dev.wms.pwrapi.utils.generalExceptions.SystemTimeoutException;
import dev.wms.pwrapi.utils.http.helpers.ResponseAndStatus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;

public class HttpClient {

    private final OkHttpClient client;

    public HttpClient() {
        this.client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl())
                .build();
    }

    public HttpClient(OkHttpClient client) {
        this.client = client;
    }

    /**
     * Makes request with OkHttp's client and parses it to Jsoup's Document. Assures proper response closing
     * @param url URL that will be requested
     * @return Jsoup's Document containing parsed html from OkHttp response
     * @throws IOException when parsing goes wrong
     */
    public Document getDocument(String url)  {
        return Jsoup.parse(doRequestAndGetString(createGet(url)));
    }

    public Document getDocument(Request request)  {
        return Jsoup.parse(doRequestAndGetString(request));
    }

    /**
     * Makes request with client and does not return anything. Needed mostly for navigating through the page
     * @param url URL that will be requested
     * @throws IOException when parsing goes wrong
     */
    public Response getResponse(String url) {
        return doRequest(createGet(url));
    }

    public Response getResponse(Request request) {
        return doRequest(request);
    }

    public Response getWithTimeout(String url, Duration duration) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(duration)
                .build();

        return client.newCall(createGet(url)).execute();
    }

    public ResponseAndStatus getStringAndStatusCode(String url){
        try(Response response = client.newCall(createGet(url)).execute()){
            return ResponseAndStatus.of(response.code(), response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String url){
        return doRequestAndGetString(createGet(url));
    }

    public String getString(Request request){
        return doRequestAndGetString(request);
    }

    private Request createGet(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    @NotNull
    private String doRequestAndGetString(Request request) {
        try(Response response = client.newCall(request).execute()){
            return response.body().string();
        } catch (SocketTimeoutException e){
            throw new SystemTimeoutException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Response doRequest(Request financeRequest) {
        try(Response response = client.newCall(financeRequest).execute()){
            return response;
        }catch (IOException ex){
            throw new RuntimeException();
        }
    }
}
