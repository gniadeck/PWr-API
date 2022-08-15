package dev.wms.pwrapi.service.eportal;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dao.eportal.EportalCalendarDAO;
import dev.wms.pwrapi.dao.eportal.EportalDAO;
import dev.wms.pwrapi.entity.eportal.MarkSummary;
import dev.wms.pwrapi.entity.eportal.calendar.CalendarMonth;
import dev.wms.pwrapi.dto.eportal.sections.EportalSection;
import dev.wms.pwrapi.utils.eportal.exceptions.WrongCourseIdException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class EportalServiceImpl implements EportalService {

    private EportalDAO generalDAO;
    private EportalCalendarDAO calendarDAO;

    @Autowired
    public EportalServiceImpl(EportalDAO generalDAO, EportalCalendarDAO calendarDAO){
        this.generalDAO = generalDAO;
        this.calendarDAO = calendarDAO;
    }

    @Override
    public String getEportalData(String login, String password) throws JsonProcessingException, IOException, LoginException {
        return generalDAO.login(login, password);
    }

    @Override
    public String getEportalKursy(String login, String password) throws JsonProcessingException, IOException, LoginException {
        return generalDAO.getEportalKursy(login, password);
    }

    @Override
    public List<EportalSection> getEportalSekcje(String login, String password, int id) throws JsonProcessingException, IOException, LoginException, WrongCourseIdException {
        return generalDAO.getEportalSekcje(login, password, id);
    }


    @Override
    public List<MarkSummary> getEportalOceny(String login, String password, int id) throws JsonProcessingException {
        return generalDAO.getEportalOceny(login, password, id);
    }


    @Override
    public CalendarMonth getEportalKalendarzOffset(String login, String password, int offset) throws IOException {
        return calendarDAO.getEventsWithOffset(login, password, offset);
    }

    @Override
    public String getEportalKalendarzIcsLink(String login, String password) throws IOException {
        return calendarDAO.getIcsCalendarUrl(login, password);
    }
}
