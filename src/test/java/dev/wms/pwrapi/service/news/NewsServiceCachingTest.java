package dev.wms.pwrapi.service.news;

import dev.wms.pwrapi.dao.news.NewsDAO;
import dev.wms.pwrapi.dto.news.FacultyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class NewsServiceCachingTest {

    @Autowired
    private NewsService newsService;
    @MockBean
    private NewsDAO newsDAO;

    @Test
    public void newsDaoShouldBeCalledOnceForMultipleCallsInTTL(){
        for(int i = 0; i < 50; i++) newsService.fetchGeneralNews();
        verify(newsDAO, times(1))
                .parsePwrRSS(any());
    }

    @Test
    public void newsDaoShouldBeCalledTwiceForTwoSeparateFacultiesInTTL(){
        for(int i = 0; i < 50; i++){
            newsService.fetchNewsForFaculty(FacultyType.ARCHITEKTURY);
            newsService.fetchNewsForFaculty(FacultyType.INFORMATYKI_I_TELEKOMUNIKACJI);
        }

        verify(newsDAO, times(1))
                .getFacultyNews(FacultyType.ARCHITEKTURY);

        verify(newsDAO, times(1))
                .getFacultyNews(FacultyType.INFORMATYKI_I_TELEKOMUNIKACJI);

    }

}
