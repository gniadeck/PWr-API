package dev.wms.pwrapi.service.events;

import dev.wms.pwrapi.dao.events.EventsDAO;
import dev.wms.pwrapi.dto.events.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final EventsDAO eventsDAO;

    @Override
    @Cacheable("pwr-events")
    public Set<EventDto> getEventsOfMonth(Optional<Month> month, Optional<Integer> year) {
        return eventsDAO.getEventsOfMonth(month, year);
    }

}
