package dev.wms.pwrapi.dao.jsos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.wms.pwrapi.dao.auth.AuthDao;
import dev.wms.pwrapi.entity.jsos.finance.FinanceEntry;
import dev.wms.pwrapi.entity.jsos.finance.FinanceResult;
import dev.wms.pwrapi.entity.jsos.finance.operations.OperationEntry;
import dev.wms.pwrapi.entity.jsos.finance.operations.FinanceOperationResult;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageFull;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageShort;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.entity.jsos.JsosStudentData;
import dev.wms.pwrapi.entity.jsos.marks.JsosMark;
import dev.wms.pwrapi.entity.jsos.marks.JsosSemester;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import okhttp3.OkHttpClient;

@Repository
@RequiredArgsConstructor
public class JsosDataDAOImpl implements JsosDataDAO {

    private final AuthDao jsosAuthDao;

    /**
     * Returns value of student's message with given internal id's from a given page number
     * @param login Login used for JSOS
     * @param password Password used for JSOS
     * @param pageNumber Page number of messages interface
     * @param messageIds Internal ids of messages (can be fetched using general /wiadomosci endpoint)
     * @return List of JsosMessageFull objects (POJO)
     * @throws IOException When some parsing goes wrong
     */
    public List<JsosMessageFull> getStudentMessage(String login, String password, int pageNumber, Integer... messageIds) {

        List<Integer> messagesIdsToVisit = new ArrayList<>(Arrays.asList(messageIds));

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = getMessagePageWithNumber(pageNumber, client);

        List<Element> rows = page.getElementsByAttributeValue("id", "listaWiadomosci").first().getElementsByTag("tr");
        rows.removeIf(row -> row.className().equals("headers"));

        List<JsosMessageShort> messages = new ArrayList<>();

        int internalId = 0;
        for(Element row : rows){

            List<Element> values = row.getElementsByTag("td");
            values.remove(0);

            JsosMessageShort message = JsosMessageShort.builder()
                    .from(values.get(0).text())
                    .subject(values.get(1).text())
                    .date(values.get(2).text())
                    .priority(values.get(3).text())
                    .internalId(internalId)
                    .read(!row.className().equals("unread"))
                    .detailsLink(row.attr("data-url"))
                    .build();

            internalId++;

            messages.add(message);

        }

        List<String> urlsToVisit = messages.stream()
                .filter(message -> messagesIdsToVisit.contains(message.getInternalId()))
                .map(JsosMessageShort::getDetailsLink).toList();

        List<JsosMessageFull> result = new ArrayList<>();
        for(String url : urlsToVisit){

            page = client.getDocument("https://jsos.pwr.edu.pl" + url);

            List<Element> messageHeaders = page.getElementsByClass("pull-left")
                    .get(1)
                    .getElementsByTag("span");


            List<Element> divs = page.getElementsByTag("div");
            divs.removeIf(div -> !div.text().contains("Treść wiadomości"));

            int urlID = messages.stream()
                    .filter(m -> m.getDetailsLink().equals(url))
                    .map(JsosMessageShort::getInternalId)
                    .findFirst()
                    .orElseThrow();

            JsosMessageFull fullMessage = JsosMessageFull.builder()
                    .from(messageHeaders.get(1).text().replace("Nadawca -", "").strip())
                    .to(messageHeaders.get(2).text().replace("Adresat -", "").strip())
                    .message(divs.get(0).text().split("Treść wiadomości")[1].strip())
                    .internalId(urlID)
                    .build();

            result.add(fullMessage);

        }

        return result;

    }


    public List<JsosMessageShort> getStudentMessages(String login, String password, int pageNumber) {

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = getMessagePageWithNumber(pageNumber, client);

        List<Element> rows = page.getElementsByAttributeValue("id", "listaWiadomosci").first().getElementsByTag("tr");
        rows.removeIf(row -> row.className().equals("headers"));

        List<JsosMessageShort> result = new ArrayList<>();

        int internalId = 0;
        for(Element row : rows){

            List<Element> values = row.getElementsByTag("td");
            values.remove(0);

            JsosMessageShort message = JsosMessageShort.builder()
                    .from(values.get(0).text())
                    .subject(values.get(1).text())
                    .date(values.get(2).text())
                    .priority(values.get(3).text())
                    .internalId(internalId)
                    .read(!row.className().equals("unread"))
                    .build();

            internalId++;
            result.add(message);

        }

        return result;
    }


    public FinanceOperationResult getStudentFinanceOperations(String login, String password) {

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/finanse/operacje");

        List<Element> details = page.getElementsByClass("box-body")
                .first()
                .getElementsByTag("li");

        FinanceOperationResult result = FinanceOperationResult.builder()
                .accountNumber(details.get(0).text().replace("Numer rachunku: ",""))
                .saldo(details.get(1).text().replace("Saldo: ", ""))
                .incomeSum(details.get(2).text().replace("Suma wpłat: ", ""))
                .outcomeSum(details.get(3).text().replace("Suma wypłat: ", ""))
                .unpaidAmount(details.get(4).text().replace("Saldo nierozliczonych zobowiązań: ",""))
                .build();

        List<OperationEntry> operations = new ArrayList<>();

        List<Element> rows = page.getElementsByAttributeValue("id", "listaWiadomosci")
                .first()
                .getElementsByTag("tr");

        rows.removeIf(row -> row.className().equals("headers"));
        rows.removeIf(row -> row.attr("style").contains("display: none"));

        for(Element row : rows){

            List<Element> rowData = row.getElementsByTag("td");

            OperationEntry entry = OperationEntry.builder()
                    .operationDate(rowData.get(0).text())
                    .value(rowData.get(1).text())
                    .title(rowData.get(2).text())
                    .paymentGateway(rowData.get(3).text())
                    .paymentAccountDetails(rowData.get(4).text())
                    .build();

            operations.add(entry);
        }

        result.setEntries(operations);
        return result;
    }

    public FinanceResult getStudentFinance(String login, String password) {

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/finanse/oplaty");


        List<Element> rows = page.getElementsByClass("data");
        List<FinanceEntry> entries = new ArrayList<>();

        for (Element row : rows) {

            List<Element> rowData = row.getElementsByTag("td");
            rowData.removeIf(data -> data.text().equals(""));

            FinanceEntry entry = FinanceEntry.builder()
                    .dataNaliczenia(rowData.get(0).text())
                    .amount(rowData.get(1).text())
                    .name(rowData.get(2).text())
                    .rata(rowData.get(3).text())
                    .terminPlatnosci(rowData.get(4).text())
                    .status(rowData.get(5).text())
                    .build();

            entries.add(entry);

        }

        String sumOfRequiredPayments = page.getElementsByClass("box-body")
                .first()
                .getElementsByTag("span")
                .text();

        return FinanceResult.builder()
                .sumOfRequiredPayments(sumOfRequiredPayments)
                .entries(entries)
                .build();

    }



    @Override
    public List<JsosSemester> getStudentMarks(String login, String password) throws LoginException {

        List<JsosSemester> result = new ArrayList<JsosSemester>();

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/indeksOceny/oceny/200");


        Element table = page.getElementsByTag("table").get(0);

        List<Element> semestersHeaders = table.getElementsByClass("semester_headers");
        List<Element> semesters = table.getElementsByClass("semester");

        for(int i = 0; i < semesters.size(); i++){

            JsosSemester toAdd = new JsosSemester();

            toAdd.setSemesterName(semestersHeaders.get(i).text());

            List<JsosMark> marks = new ArrayList<JsosMark>();

            List<Element> rowMarks = semesters.get(i).getElementsByTag("tr");

            for(Element row : rowMarks){

                List<Element> tds = row.getElementsByTag("td");

                JsosMark mark = JsosMark.builder()
                    .prowadzacy(tds.get(0).text())
                    .kodKursu(row.getElementsByAttributeValue("data-title", "kodKursu").text())
                    .nazwaKursu(row.getElementsByAttributeValue("data-title", "nazwaKursu").text())
                    .formaZajec(tds.get(3).getElementsByClass("ankietaLabel").text())
                    .ZZU(tds.get(4).text())
                    .ocena(row.getElementsByAttributeValue("title", "ocena").text())
                    .data(tds.get(6).text())
                    .ects(tds.get(7).text())
                    .build();
                marks.add(mark);
            }

            toAdd.setMarks(marks);
            result.add(toAdd);
        }
        return result;
    }

    @Override
    public JsosStudentData getStudentData(String login, String password) throws LoginException {

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/indeksDane");

        Element table = page.getElementById("design_1");

        List<Element> data = table.getElementsByTag("td");

        return JsosStudentData.builder()
                .wydzial(data.get(0).text())
                .kierunek(data.get(1).text())
                .specjalnosc(data.get(2).text())
                .stopien(data.get(3).text())
                .numberAlbumu(data.get(4).text())
                .imiona(data.get(5).text())
                .nazwisko(data.get(6).text())
                .imieOjca(data.get(7).text())
                .dataUrodzenia(data.get(8).text())
                .miejsceUrodzenia(data.get(9).text())
                .build();
    }

    @NotNull
    private Document getMessagePageWithNumber(int pageNumber, HttpClient client) {
        Document page;
        if(pageNumber == 1){
            page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/wiadomosci");
        } else {
            page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/wiadomosci/" + pageNumber);
        }
        return page;
    }

}
