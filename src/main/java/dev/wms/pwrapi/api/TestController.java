package dev.wms.pwrapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Used for DevOps testing purposes, will be removed soon.
 */
@RestController
@RequestMapping("/api/test")
@Deprecated(forRemoval = true)
public class TestController {

    @GetMapping("/jsos")
    public String test() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        return client.send(HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create("http://isjsosdown-backend:8081/api/test"))
                        .build(), HttpResponse.BodyHandlers.ofString()).body().toString();
    }
}
