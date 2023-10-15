package dev.wms.pwrapi.service.jsos;

import dev.wms.pwrapi.dao.auth.AuthDao;
import dev.wms.pwrapi.dao.jsos.JsosDataDAO;
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
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JsosServiceImpl implements JsosService {

    private AuthDao jsosAuthDao;
    private JsosDataDAO dataDAO;
    private JsosLessonsDAO lessonsDAO;

    @Autowired
    public JsosServiceImpl(AuthDao jsosAuthDao, JsosDataDAO dataDAO, JsosLessonsDAO lessonsDAO) {
        this.jsosAuthDao = jsosAuthDao;
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
    public List<JsosSemester> getStudentMarks(String login, String password) throws LoginException {

        return dataDAO.getStudentMarks(login, password);

    }

    @Override
    public void login(String login, String password) throws LoginException {
        jsosAuthDao.login(login, password);
    }

    @Override
    public JsosStudentData getStudentData(String login, String password) throws LoginException {

        JsosStudentData result = null;

        result = dataDAO.getStudentData(login, password);

        return result;

    }

    @Override
    public FinanceResult getStudentFinanse(String login, String password) {
        return dataDAO.getStudentFinance(login, password);
    }

    @Override
    public FinanceOperationResult getStudentFinanceOperations(String login, String password) {
        return dataDAO.getStudentFinanceOperations(login, password);
    }

    @Override
    public List<JsosMessageShort> getStudentMessagesList(String login, String password, int page) {
        return dataDAO.getStudentMessages(login, password, page);
    }

    @Override
    public List<JsosMessageFull> getStudentMessage(String login, String password, int page, Integer... ids) {
        return dataDAO.getStudentMessage(login, password, page, ids);
    }


}
