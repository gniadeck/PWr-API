package dev.wms.pwrapi.dao.events;

import dev.wms.pwrapi.dto.events.EventDto;
import dev.wms.pwrapi.utils.http.HttpClient;
import okhttp3.OkHttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Repository
public class EventsDAOImpl implements EventsDAO {

    @Override
    public Set<EventDto> getEventsOfMonth(Optional<Month> month, Optional<Integer> year) {
        return toEventDtoSet("https://pwr.edu.pl/uczelnia/przed-nami" + "/month," + parseMonth(month) + "-" +
                parseYear(year) + ".html");
    }

    private int parseYear(Optional<Integer> year){
        return year.orElse(LocalDate.now().getYear());
    }


    private String parseMonth(Optional<Month> month){
        int monthNumber = month.map(monthOptional -> monthOptional.getValue())
                .orElse(LocalDate.now().getMonth().getValue());
        return monthNumber < 10 ? "0" + monthNumber : String.valueOf(monthNumber);
    }


    private Set<EventDto> toEventDtoSet(String url){

        var client = new HttpClient();

        Document document = client.getDocument(url);

        Set<EventDto> eventDtos = new LinkedHashSet<>();

        Elements events = document.getElementsByClass("fc-evnt");
        for(Element event: events){

            EventDto eventDto = new EventDto();
            eventDto.setTitle(event.ownText());

            Elements eventDetails = event.getElementsByClass("fc-evnt-info").get(0)
                    .getElementsByClass("text").get(0).getElementsByTag("p");

            for(Element eventDetail: eventDetails){
                parseDetails(eventDto, eventDetail);
            }

            eventDtos.add(eventDto);
        }

        return eventDtos;

    }

    private void parseDetails(EventDto eventDtoToExtend, Element eventDetail){

        Element typeOfDetail = eventDetail.getElementsByTag("strong").first();

        if(typeOfDetail == null)
            eventDtoToExtend.setDescription(eventDetail.text());
        else{
            switch (typeOfDetail.text()) {
                case "Data:" -> eventDtoToExtend.setDate(eventDetail.ownText());
                case "Godzina:" -> eventDtoToExtend.setTime(eventDetail.ownText());
                case "Miejsce wydarzenia:" -> eventDtoToExtend.setPlace(eventDetail.ownText());
            }
        }
    }

}
