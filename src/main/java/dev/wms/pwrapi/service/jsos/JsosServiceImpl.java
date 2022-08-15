package dev.wms.pwrapi.service.jsos;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.wms.pwrapi.dao.jsos.JsosDataDAO;
import dev.wms.pwrapi.dao.jsos.JsosGeneralDAO;
import dev.wms.pwrapi.dao.jsos.JsosLessonsDAO;
import dev.wms.pwrapi.entity.jsos.JsosLesson;
import dev.wms.pwrapi.entity.jsos.JsosStudentData;
import dev.wms.pwrapi.entity.jsos.finance.FinanceResult;
import dev.wms.pwrapi.entity.jsos.finance.operations.FinanceOperationResult;
import dev.wms.pwrapi.entity.jsos.marks.JsosSemester;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageFull;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageShort;
import dev.wms.pwrapi.entity.jsos.weeks.JsosDay;
import dev.wms.pwrapi.entity.jsos.weeks.JsosWeek;
import dev.wms.pwrapi.dto.jsos.JsosConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.wms.pwrapi.utils.generalExceptions.LoginException;

@Service
public class JsosServiceImpl implements JsosService {

    private JsosGeneralDAO generalDAO;
    private JsosDataDAO dataDAO;
    private JsosLessonsDAO lessonsDAO;

    @Autowired
    public JsosServiceImpl(JsosGeneralDAO generalDAO, JsosDataDAO dataDAO, JsosLessonsDAO lessonsDAO) {
        this.generalDAO = generalDAO;
        this.dataDAO = dataDAO;
        this.lessonsDAO = lessonsDAO;
    }

    @Override
    public JsosWeek getOffsetLessons(String login, String password, int offset) throws IOException, LoginException{
        return lessonsDAO.getOffsetWeekLessons(login, password, offset);
    }

    @Override
    public JsosDay getTodaysLessions(String login, String password) throws IOException, LoginException, dev.wms.pwrapi.utils.jsos.exceptions.NoTodayClassException{
        return lessonsDAO.getTodaysLessons(login, password);
    }

    @Override
    public JsosDay getTomorrowLessons(String login, String password) throws IOException, LoginException{
        return lessonsDAO.getTomorrowLessons(login, password);
    }

    @Override
    public JsosWeek getThisWeekLessons(String login, String password) throws IOException, LoginException{
        return lessonsDAO.getThisWeekLessons(login, password);
    }

    @Override
    public JsosWeek getNextWeekLessons(String login, String password) throws IOException, LoginException{
        return lessonsDAO.getNextWeekLessons(login, password);
    }

    @Override
    public List<JsosLesson> getAllStudentsLessons(String login, String password) throws IOException, LoginException{
        return lessonsDAO.getAllLessons(login, password);
    }

    @Override
    public List<JsosSemester> getStudentMarks(String login, String password)
            throws JsonProcessingException, IOException, LoginException {

        return dataDAO.getStudentMarks(login, password);

    }

    @Override
    public JsosConnection login(String login, String password) throws LoginException {

        JsosConnection result;

        try {
            result = generalDAO.login(login, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public JsosStudentData getStudentData(String login, String password) throws LoginException {

        JsosStudentData result = null;

        try {
            result = dataDAO.getStudentData(login, password);
        } catch (IOException i) {
            i.printStackTrace();
        }

        return result;

    }

    @Override
    public FinanceResult getStudentFinanse(String login, String password) throws IOException {
        return dataDAO.getStudentFinance(login, password);
    }

    @Override
    public FinanceOperationResult getStudentFinanceOperations(String login, String password) throws IOException {
        return dataDAO.getStudentFinanceOperations(login, password);
    }

    @Override
    public List<JsosMessageShort> getStudentMessagesList(String login, String password, int page) throws IOException {
        return dataDAO.getStudentMessages(login, password, page);
    }

    @Override
    public List<JsosMessageFull> getStudentMessage(String login, String password, int page, Integer... ids) throws IOException {
        return dataDAO.getStudentMessage(login, password, page, ids);
    }


}
