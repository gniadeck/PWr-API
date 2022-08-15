package dev.wms.pwrapi.service.jsos;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import dev.wms.pwrapi.utils.generalExceptions.LoginException;

import java.io.IOException;
import java.util.List;

public interface JsosService {
    JsosWeek getOffsetLessons(String login, String password, int offset) throws IOException, LoginException;

    JsosDay getTodaysLessions(String login, String password) throws IOException, LoginException, dev.wms.pwrapi.utils.jsos.exceptions.NoTodayClassException;

    JsosDay getTomorrowLessons(String login, String password) throws IOException, LoginException;

    JsosWeek getThisWeekLessons(String login, String password) throws IOException, LoginException;

    JsosWeek getNextWeekLessons(String login, String password) throws IOException, LoginException;

    List<JsosLesson> getAllStudentsLessons(String login, String password) throws IOException, LoginException;

    List<JsosSemester> getStudentMarks(String login, String password)
            throws JsonProcessingException, IOException, LoginException;

    JsosConnection login(String login, String password) throws LoginException;

    JsosStudentData getStudentData(String login, String password) throws LoginException;
    FinanceResult getStudentFinanse(String login, String password) throws IOException;
    FinanceOperationResult getStudentFinanceOperations(String login, String password) throws IOException;
    List<JsosMessageShort> getStudentMessagesList(String login, String password, int page) throws IOException;
    List<JsosMessageFull> getStudentMessage(String login, String password, int page, Integer... ids) throws IOException;
}
