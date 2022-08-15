package dev.wms.pwrapi.utils.jsos;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import dev.wms.pwrapi.utils.http.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class JsosLessonsUtils {


    /**
     * Just a utils method for determining kind based on the one letter code
     * @param className
     * @return
     */
    public static String determineKindFromClassName(String className){

        String result;

        List<String> availableClasses = Arrays.asList(new String[]{"W","C","L","P","S","I"});
        List<String> availableResults = Arrays.asList(new String[]{"Wykład", "Ćwiczenia", "Laboratorium", "Projekt", "Seminarium", "Inne"});
        String base = "rozklady_";
        className = className.replace(base, "").split(" ")[0].strip();

        int index = availableClasses.indexOf(className);

        if(index == -1){
            return "Unidentified type. Please contanct API support";
        } else {
            return availableResults.get(availableClasses.indexOf(className));
        }

    }

    /**
     * Fetch url of next week
     * @param doc Document where we should look for button
     * @return URL of next week
     */
    public static String getUrlOfNextWeek(Document doc){

        String result;
        result = "https://jsos.pwr.edu.pl" + doc.getElementsByClass("bx-next").get(0).attr("href");

        return result;
    }

    /**
     * Fetch url of week with given offset.
     *
     * Moves <b>forward</b> based on an offset
     * @param doc Document where we should look for button
     * @param client Logged client for performing requests
     * @param offset How many times go forward, <b>can't be negative for going back</b>
     * @return URL of offseted week
     * @throws IOException If parsing goes wrong
     */
    public static String getUrlOfNextWeek(Document doc, OkHttpClient client, int offset) throws IOException{

        if(offset < 0){
            throw new RuntimeException("Offset cannot be negative in next week method!");
        }

        String result = "https://jsos.pwr.edu.pl/index.php/student/zajecia/tydzien";


        for(int i = 0; i < offset; i++){
            result = "https://jsos.pwr.edu.pl" + doc.getElementsByClass("bx-next").get(0).attr("href");
            doc = HttpUtils.makeRequestWithClientAndGetDocument(client, result);
        }

        return result;
    }

    /**
     * Fetch url of previous week
     * @param doc Document where we should look for a button
     * @return URL of previous week
     */
    public static String getUrlOfPreviousWeek(Document doc){

        String result;
        result = "https://jsos.pwr.edu.pl" + doc.getElementsByClass("bx-prev").get(0).attr("href");

        return result;
    }

    /**
     * Fetch url of previous week based on the given offset
     * Moves <b>back</b> based on an offset
     * @param doc Document where we should look for button
     * @param client Logged client for performing requests
     * @param offset How many times go back
     * @return URL of offseted week
     * @throws IOException If parsing goes wrong
     */
    public static String getUrlOfPreviousWeek(Document doc, OkHttpClient client, int offset) throws IOException{

        String result = "https://jsos.pwr.edu.pl/index.php/student/zajecia/tydzien";
        if(offset == 0) return getUrlOfPreviousWeek(doc);

        for(int i = 0; i < offset; i++) {
            result = "https://jsos.pwr.edu.pl" + doc.getElementsByClass("bx-prev").get(0).attr("href");
            doc = HttpUtils.makeRequestWithClientAndGetDocument(client, result);
        }

        return result;

    }  
    
}
