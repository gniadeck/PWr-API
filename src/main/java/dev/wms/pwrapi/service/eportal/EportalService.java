package dev.wms.pwrapi.service.eportal;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.entity.eportal.MarkSummary;
import dev.wms.pwrapi.entity.eportal.calendar.CalendarMonth;
import dev.wms.pwrapi.dto.eportal.sections.EportalSection;
import dev.wms.pwrapi.utils.eportal.exceptions.WrongCourseIdException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;

import java.io.IOException;
import java.util.List;

public interface EportalService {
    String getEportalData(String login, String password) throws JsonProcessingException, IOException, LoginException;

    String getEportalKursy(String login, String password) throws JsonProcessingException, IOException, LoginException;

    List<EportalSection> getEportalSekcje(String login, String password, int id) throws JsonProcessingException, IOException, LoginException, WrongCourseIdException;

    List<MarkSummary> getEportalOceny(String login, String password, int id) throws JsonProcessingException;

    CalendarMonth getEportalKalendarzOffset(String login, String password, int offset) throws IOException;

    String getEportalKalendarzIcsLink(String login, String password) throws IOException;
}
