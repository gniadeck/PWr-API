package dev.wms.pwrapi.api;

import dev.wms.pwrapi.dto.news.Channel;
import dev.wms.pwrapi.dto.news.FacultyType;
import dev.wms.pwrapi.service.news.NewsService;
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
    public ResponseEntity<Channel> fetchGeneralNews(){
        return ResponseEntity.ok(newsService.fetchGeneralNews());
    }

    @GetMapping("/faculty")
    public ResponseEntity<Channel> fetchFacultyNews(
            @RequestParam FacultyType faculty){
        return ResponseEntity.ok(newsService.fetchNewsForFaculty(faculty));
    }

}
