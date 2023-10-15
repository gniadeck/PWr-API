package dev.wms.pwrapi.service.events;

import dev.wms.pwrapi.dao.events.EventsDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Month;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class EventsServiceCachingTests {

    @Autowired
    EventsService eventsService;

    @MockBean
    EventsDAO eventsDAO;

    @Test
    public void givenEventServiceWhenAskingForSameDataFewTimesThenDAOMethodCalledOnce(){
        for (int i=0; i<10; i++){
            eventsService.getEventsOfMonth(Optional.of(Month.JANUARY), Optional.of(2023));
        }
        verify(eventsDAO, times(1)).getEventsOfMonth(Optional.of(Month.JANUARY), Optional.of(2023));
    }

    @Test
    public void givenEventServiceWhenAskingForNotSameDataBothOneThenDAOMethodCalledOnceForBoth(){
        eventsService.getEventsOfMonth(Optional.of(Month.DECEMBER), Optional.of(2023));
        eventsService.getEventsOfMonth(Optional.of(Month.AUGUST), Optional.of(2023));
        verify(eventsDAO, times(1)).getEventsOfMonth(Optional.of(Month.DECEMBER), Optional.of(2023));
        verify(eventsDAO, times(1)).getEventsOfMonth(Optional.of(Month.AUGUST), Optional.of(2023));
    }

}
