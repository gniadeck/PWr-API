package dev.wms.pwrapi.service.events;

import dev.wms.pwrapi.dto.events.EventDto;

import java.time.Month;
import java.util.Optional;
import java.util.Set;

public interface EventsService {

    Set<EventDto> getEventsOfMonth(Optional<Month> month, Optional<Integer> year);

}
