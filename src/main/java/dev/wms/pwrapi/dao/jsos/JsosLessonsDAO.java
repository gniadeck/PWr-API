package dev.wms.pwrapi.dao.jsos;

import dev.wms.pwrapi.entity.jsos.JsosLesson;
import dev.wms.pwrapi.entity.jsos.weeks.JsosDay;
import dev.wms.pwrapi.entity.jsos.weeks.JsosWeek;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.jsos.exceptions.NoTodayClassException;

import java.io.IOException;
import java.util.List;

public interface JsosLessonsDAO {
    /**
     * Returns today's student's today lessons
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @return JsosDay object
     * @throws IOException If parsing goes wrong
     * @throws LoginException If logging in goes wrong
     * @throws NoTodayClassException If somehow there is now today day in JSOS
     */
    JsosDay getTodaysLessons(String login, String password) throws IOException, LoginException, NoTodayClassException;

    /**
     * Returns tommorow's students lessons
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @return JsosDay object
     * @throws IOException If parsing goes wrong
     * @throws LoginException If logging in goes wrong
     */
    JsosDay getTomorrowLessons(String login, String password) throws IOException, LoginException;

    /**
     * Returns this week lessons
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @return JsosWeek object
     * @throws IOException If parsing goes wrong
     * @throws LoginException If logging in goes wrong
     */
    JsosWeek getThisWeekLessons(String login, String password) throws IOException, LoginException;
    /**
     * Returns next week lessons
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @return JsosWeek object
     * @throws IOException If parsing goes wrong
     * @throws LoginException If logging in goes wrong
     */
    JsosWeek getNextWeekLessons(String login, String password) throws IOException, LoginException;

    /**
     * Returns week lessons offseted by given offset (skips weeks back or forth in order to access the desired one)
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @param offset How many weeks to go back or forth (supports also negative values for back forwarding)
     * @return JsosWeek object
     * @throws IOException If parsing goes wrong
     * @throws LoginException If logging in goes wrong
     */

    JsosWeek getOffsetWeekLessons(String login, String password, int offset) throws IOException, LoginException;

    /**
     * Returns all students lessons (beware of even and odd weeks)
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @return List of JsosLesson objects
     * @throws IOException If parsing goes wrong
     * @throws LoginException If logging in goes wrong
     */
    List<JsosLesson> getAllLessons(String login, String password) throws IOException, LoginException;
}
