package dev.wms.pwrapi.service.news;

import dev.wms.pwrapi.dao.news.GeneralNewsDAO;
import dev.wms.pwrapi.dto.news.Channel;
import dev.wms.pwrapi.dto.news.FacultyType;
import dev.wms.pwrapi.dto.news.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class NewsService {

    private GeneralNewsDAO generalNewsDAO;


    public Channel fetchGeneralNews() {
        return generalNewsDAO.parsePwrRSS("https://pwr.edu.pl/rss/pl/24.xml");
    }

    public Channel fetchNewsForFaculty(FacultyType faculty) {
        return generalNewsDAO.getFacultyNews(faculty);
    }
}
