package dev.wms.pwrapi.service.news;

import dev.wms.pwrapi.dto.news.FacultyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Test
    @Disabled("Disabled because of server protection against foreign IPs (GitHub actions usage)")
    public void newsServiceFetchShouldBeNotEmptyForEveryFaculty(){
        for (FacultyType faculty : FacultyType.values()) {
            assertFalse(newsService.fetchNewsForFaculty(faculty).getItem().isEmpty());
        }
    }

    @Test
    @Disabled("Disabled because of server protection against foreign IPs (GitHub actions usage)")
    public void newsServiceShouldFetchGeneralNews(){
        assertFalse(newsService.fetchGeneralNews().getItem().isEmpty());
    }

}
