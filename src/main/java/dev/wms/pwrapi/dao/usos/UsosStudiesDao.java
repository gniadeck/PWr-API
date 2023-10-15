package dev.wms.pwrapi.dao.usos;

import dev.wms.pwrapi.dto.usos.UsosCourse;
import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.utils.common.URLValidator;
import dev.wms.pwrapi.utils.http.HttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

public class UsosStudiesDao {

    private static final String NO_MARKS_TEXT = "(brak ocen)";
    private final HttpClient client;
    private static final Pattern MARK_PATTERN = Pattern.compile("Ocena: \\d.\\d");

    public UsosStudiesDao(HttpClient client) {
        this.client = client;
    }

    public List<UsosStudies> parseStudies(){
        var studies = getStudies();
        enhanceWithMarks(studies);
        return studies;
    }

    private void enhanceWithMarks(List<UsosStudies> studies) {
        new UsosMarksProxy(client).enhanceWithMarks(studies);
    }

    private List<UsosStudies> getStudies() {
        var website = getUnparsedMarks();
        var element = website.getElementsByClass("usos-ui").first();
        return getStudiesParameters(element);
    }

    private Document getUnparsedMarks() {
        return client.getDocument("https://web.usos.pwr.edu.pl/kontroler.php?_action=dla_stud/studia/oceny/index");
    }


    private List<UsosStudies> getStudiesParameters(Element element) {
        Map<Element, Element> studiesWithMarks = getStudiesWithMarks(element);

        return studiesWithMarks.entrySet().stream()
                .map(this::parseStudies)
                .toList();
    }

    private UsosStudies parseStudies(Map.Entry<Element, Element> studies){
        var parsedDescription = parseDescription(studies.getKey());
        return UsosStudies.builder()
                .level(parsedDescription.level())
                .name(parsedDescription.name())
                .type(parsedDescription.type())
                .semesters(parseSemesters(studies.getValue()))
                .build();
    }

    private List<UsosSemester> parseSemesters(Element marks) {
        List<UsosSemester> result = new ArrayList<>();
        var semesters = marks.select(".expand-collapse.collapsed, .expand-collapse");
        for(var semester : semesters){
            var semesterHeader = semester.getElementsByClass("ec-header").first();
            var temp = (UsosSemester.builder()
                    .name(semesterHeader.text())
                    .courses(parseCourses(semester))
                    .decisionUrls(getDecisionsLinks(semester))
                    .build());
            if (temp.decisionUrls().isPresent()) {
                result.add(temp);
            }
        }
        return result;
    }


    private Optional<Set<String>> getDecisionsLinks(Element semester) {
        try {
            Set<String> decisionLinks = new HashSet<>();
            var columns = semester.getElementsByTag("td");
            decisionLinks.add(columns.get(3).getElementsByTag("a").toString().split("\"")[1].replace("amp;", ""));
            decisionLinks.removeIf(url -> !URLValidator.isValidUrl(url));
            return Optional.of(decisionLinks);
        } catch (Throwable exepction){
            return Optional.empty();
        }
    }

    private List<UsosCourse> parseCourses(Element semester) {
        return semester.getElementsByTag("tr").stream()
                .skip(1)
                .filter(row -> !row.text().contains(NO_MARKS_TEXT))
                .map(this::parseCourseRow)
                .toList();
    }

    private UsosCourse parseCourseRow(Element row) {
        var columns = row.getElementsByTag("td");
        String[] splittedName = columns.first().text().split("\\[");
        String mark = parseMark(columns.get(2));
        return UsosCourse.builder()
                .name(splittedName[0])
                .code(splittedName.length >= 2 ? splittedName[1].replace("]","") : "")
                .mark(BigDecimal.valueOf(Double.parseDouble(mark)))
                .build();
    }

    private String parseMark(Element element) {
        var matcher = MARK_PATTERN.matcher(element.text());
        matcher.find();
        String foundString = matcher.group();
        return foundString.replace("Ocena:", "").replace(",", ".").trim();
    }

    private Map<Element, Element> getStudiesWithMarks(Element element) {
        Map<Element, Element> result = new HashMap<>();
        var studiesDescriptions = element.getElementsByAttributeValue("style", "margin: 1rem 0 0 0");
        var studiesMarks = element.getElementsByAttributeValue("id", "oceny");
        for (int i = 0; i < Math.min(studiesDescriptions.size(), studiesMarks.size()); i++) {
            result.put(studiesDescriptions.get(i).getElementsByTag("h2").first(), studiesMarks.get(i));
        }

        return result;
    }

    private UsosStudies parseDescription(Element description) {
        var splittedString = description.text().split(",");
        return new UsosStudies(splittedString[0].trim(), splittedString[1].trim(),
                splittedString[2].trim(), new ArrayList<>());
    }

}
