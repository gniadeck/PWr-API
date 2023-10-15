package dev.wms.pwrapi.jsos;

import dev.wms.pwrapi.dao.auth.AuthDao;
import dev.wms.pwrapi.testingUtils.TestUtils;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.http.HttpClient;
import dev.wms.pwrapi.utils.jsos.JsosLessonsUtils;
import okhttp3.OkHttpClient;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsClassesTests {

    @Autowired
    AuthDao jsosAuthDao;

    @Test
    public void jsosLessonsUtilsNameFromClassMethodTest(){

        List<String> availableClasses = Arrays.asList(new String[]{"W","C","L","P","S","I"});
        List<String> availableResults = Arrays.asList(new String[]{"Wykład", "Ćwiczenia", "Laboratorium", "Projekt", "Seminarium", "Inne"});

        for(int i = 0; i < availableClasses.size(); i++){
            assertEquals(JsosLessonsUtils.determineKindFromClassName(availableClasses.get(i)), availableResults.get(i));
        }
    }

    @Test
    public void jsosLessonsUtilsNameFromClassMethodTestWithPrefix(){

        List<String> availableClasses = Arrays.asList(new String[]{"W","C","L","P","S","I"});
        List<String> availableResults = Arrays.asList(new String[]{"Wykład", "Ćwiczenia", "Laboratorium", "Projekt", "Seminarium", "Inne"});

        for(int i = 0; i < availableClasses.size(); i++){
            assertEquals(JsosLessonsUtils.determineKindFromClassName("rozklady_" + availableClasses.get(i)), availableResults.get(i));
        }
    }

    @Test
    public void jsosLessonsUtilsNameFromClassMethodShouldNotThrowExceptionOnWeirdClass(){

        List<String> availableClasses = Arrays.asList(new String[]{"W","C","L","P","S","I"});
        List<String> availableResults = Arrays.asList(new String[]{"Wykład", "Ćwiczenia", "Laboratorium", "Projekt", "Seminarium", "Inne"});

        for(int i = 0; i < availableClasses.size(); i++){
            assertEquals(JsosLessonsUtils.determineKindFromClassName("rozklady_" + availableClasses.get(i)), availableResults.get(i));
            assertDoesNotThrow(() -> JsosLessonsUtils.determineKindFromClassName(TestUtils.generateRandomString(10)));
        }
    }


    @Test
    public void getUrlOfNextWeekShouldThrowExceptionOnNegativeOffset(){

        assertThrows(RuntimeException.class, () ->
                JsosLessonsUtils.getUrlOfNextWeek(new Document(""),
                        new HttpClient(),
                        -1));

    }

    @Test
    @Disabled("This test cases require good response timings from webiste host")
    public void loginUtilThrowsExceptionOnWrongLoginAndPassword(){

        assertThrows(LoginException.class, () ->
                jsosAuthDao.login("omgImSuchABadLogin", "omgImSuchABadPassword"));

    }




}
