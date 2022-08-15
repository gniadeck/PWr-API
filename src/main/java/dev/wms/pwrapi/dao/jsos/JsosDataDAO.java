package dev.wms.pwrapi.dao.jsos;

import dev.wms.pwrapi.entity.jsos.finance.FinanceResult;
import dev.wms.pwrapi.entity.jsos.finance.operations.FinanceOperationResult;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageFull;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageShort;
import dev.wms.pwrapi.entity.jsos.marks.JsosSemester;
import dev.wms.pwrapi.entity.jsos.JsosStudentData;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;

import java.io.IOException;
import java.util.List;

public interface JsosDataDAO {

    /**
     * Returns all student's marks (limit is 200 semesters)
     * @param login Login used for JSOS
     * @param password Password used for JSOS
     * @return List of JsosSemester objects. Each of them contains marks for given semester
     * @throws IOException When some parsing goes wrong
     * @throws LoginException If password is wrong
     */
    List<JsosSemester> getStudentMarks(String login, String password)
            throws IOException, LoginException;

    /**
     * Returns student data available in "Dane" JSOS's page
     * @param login Login used for JSOS
     * @param password Password used for JSOS
     * @return StudentData object, which contains <b>personal information</b>
     * @throws IOException When some parsing goes wrong
     * @throws LoginException If password is wrong
     */
    JsosStudentData getStudentData(String login, String password) throws IOException, LoginException;

    /**
     * Return's student financial operations from /finanse page
     * @param login Login used for JSOS
     * @param password Password used for JSOS
     * @return FinanceOperationResult object containing list of all operations on given account
     * @throws IOException
     */
    FinanceOperationResult getStudentFinanceOperations(String login, String password) throws IOException;
    FinanceResult getStudentFinance(String login, String password) throws IOException;
    /**
     * Returns value of student's message with given internal id's from a given page number
     * @param login Login used for JSOS
     * @param password Password used for JSOS
     * @param pageNumber Page number of messages interface
     * @param messageIds Internal ids of messages (can be fetched using general /wiadomosci endpoint)
     * @return List of JsosMessageFull objects (POJO)
     * @throws IOException When some parsing goes wrong
     */
    List<JsosMessageFull> getStudentMessage(String login, String password, int pageNumber, Integer... messageIds) throws IOException;

    /**
     * Returns student's messages from given page number. Can be used to obtain message's internal ID
     * @param login Login used for JSOS
     * @param password Password used for JSOS
     * @param pageNumber Page number of messages interface
     * @return List of JsosMessageShort objects (POJO)
     * @throws IOException When some parsing goes wrong
     */
    List<JsosMessageShort> getStudentMessages(String login, String password, int pageNumber) throws IOException;
}
