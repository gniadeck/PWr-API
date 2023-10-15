package dev.wms.pwrapi.dao.events;

import dev.wms.pwrapi.dto.events.EventDto;

import java.time.Month;
import java.util.Optional;
import java.util.Set;

public interface EventsDAO {

    Set<EventDto> getEventsOfMonth(Optional<Month> month, Optional<Integer> year);

}
