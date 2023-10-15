package dev.wms.pwrapi.service.news;

import dev.wms.pwrapi.dao.news.NewsDAO;
import dev.wms.pwrapi.dto.news.Channel;
import dev.wms.pwrapi.dto.news.FacultyType;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsService {

    private NewsDAO newsDAO;

    @Cacheable("pwr-news")
    public Channel fetchGeneralNews() {
        return newsDAO.parsePwrRSS("https://pwr.edu.pl/rss/pl/24.xml");
    }

    @Cacheable("pwr-news")
    public Channel fetchNewsForFaculty(FacultyType faculty) {
        return newsDAO.getFacultyNews(faculty);
    }
}
