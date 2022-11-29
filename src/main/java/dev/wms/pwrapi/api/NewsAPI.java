package dev.wms.pwrapi.api;

import dev.wms.pwrapi.dto.news.Channel;
import dev.wms.pwrapi.dto.news.FacultyType;
import dev.wms.pwrapi.service.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/news", produces = "application/json")
@AllArgsConstructor
public class NewsAPI {

    private NewsService newsService;

    @GetMapping("/general")
    @Operation(summary = "Returns recent news from main PWr website",
    description = "This endpoint implementation is based on RSS scraping, transforming and caching. " +
            "RSS is taken from https://pwr.edu.pl/rss/pl/24.xml and which, as we observed, is a copy of news available on " +
            "https://pwr.edu.pl/uczelnia/aktualnosci The data is cached for 15 minutes from last call, so recent news can be displayed with maximum 15 " +
            "minutes delay.")
    public ResponseEntity<Channel> fetchGeneralNews(){
        return ResponseEntity.ok(newsService.fetchGeneralNews());
    }

    @GetMapping("/faculty")
    @Operation(summary = "Returns recent news from faculty website",
    description = "Implementation of this endpoint is based on RSS scraping or HTTP scraping. If the website supports RSS, " +
            "rss is used, if not, the traditional HTTP scraping and HTML parsing is used. Detailed info about website and " +
            "used method for certain faculties can be viewed here https://github.com/komp15/PWr-API/blob/feature_newsAPI/src/main/java/dev/wms/pwrapi/dto/news/FacultyType.java " +
            "The data is cached for 15 minutes from last call for all faculties, so recent news can be displayed with maximum 15 " +
            "minutes delay.")
    public ResponseEntity<Channel> fetchFacultyNews(
            @RequestParam FacultyType faculty){
        return ResponseEntity.ok(newsService.fetchNewsForFaculty(faculty));
    }

}
