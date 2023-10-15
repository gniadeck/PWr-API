package dev.wms.pwrapi.dao.edukacja;

import dev.wms.pwrapi.entity.edukacja.Group;
import dev.wms.pwrapi.entity.edukacja.Subject;
import dev.wms.pwrapi.scrapper.edukacja.EdukacjaScrapperService;
import dev.wms.pwrapi.utils.edukacja.exceptions.EnrollmentAccessDeniedException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import okhttp3.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EduSubjectDAOImpl implements EduSubjectDAO {

    private WebDriver driver;
    private final OkHttpClient client;
    private final EdukacjaScrapperService scrapperServices;

    public EduSubjectDAOImpl(EdukacjaScrapperService scrapperServices){
        this.scrapperServices = scrapperServices;
        client = new OkHttpClient();
    }


    @Override
    public List<Subject> doFetchSubjects(String login, String password) {
        driver = scrapperServices.login(login, password);

        // try to log into edukacja with given credential, if login fails throw login exceptio
        try {
            // fail is recognized by its message which then'll be present
            String potentialLoginError = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/b[2]/font")).getText();
            // if its found, login should be thrown
            throw new LoginException();
        }catch(NoSuchElementException ignored){
        }

        // click zapisy button
        driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[1]/table/tbody/tr[1]/td/table[2]/tbody/tr[15]/td/a")).click();


        String prawoDoZapisow = driver.findElement(By.xpath("//*[@id=\"GORAPORTALU\"]/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[5]/tbody/tr[4]/td")).getText();
        if(prawoDoZapisow.contains("Brak prawa"))
            throw new EnrollmentAccessDeniedException();

        // most recent enrollment fetch
        driver.findElement(By.cssSelector("#GORAPORTALU > tbody > tr:nth-child(4) > td > table > tbody > tr:nth-child(1) > td.PRAWA_KOMORKA > table > tbody > tr > td > table:nth-child(16) > tbody > tr:nth-child(3) > td:nth-child(1) > a")).click();

        // working enrollment fetch (for testing purposes)
        // driver.findElement(By.cssSelector("#GORAPORTALU > tbody > tr:nth-child(4) > td > table > tbody > tr:nth-child(1) > td.PRAWA_KOMORKA > table > tbody > tr > td > table:nth-child(16) > tbody > tr:nth-child(5) > td:nth-child(1) > a")).click();

        // search for current unit enrollment, fetch all possible enrollments
        List<WebElement> trTable = driver.findElements(By.xpath("//*[@id=\"GORAPORTALU\"]/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[8]/tbody/tr"));

        for(int i = 0; i < trTable.size(); i++){
            // search for unit - specific enrollment
            WebElement tr = trTable.get(i);
            String enrollmentType = tr.findElement(By.cssSelector("td:nth-child(2)")).getText();
            if(enrollmentType.contains("Wydział")){
                // search for units button
                tr.findElement(By.cssSelector("td:last-child > table > tbody > tr > td:last-child > input:last-child")).click();
                i = trTable.size();
            }
        }

        Select filter = new Select(driver.findElement(By.name("KryteriumFiltrowania")));
        filter.selectByVisibleText("Z wektora zapisowego, do których słuchacz ma uprawnienia");

        // fetch data from first row of table
        // get number of pages
        List<WebElement> pagination = new ArrayList<>(driver.findElements(By.className("paging-numeric-btn")));
        List<Subject> subjects = new ArrayList<>();
        int i = 2;
        int j = 0;
        while (true) {
            try {

                Subject toAdd = new Subject();
                toAdd.setId(driver.findElement(By.xpath(
                                "/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[6]/tbody/tr["
                                        + i + "]/td[1]/a"))
                        .getText());
                toAdd.setName(driver.findElement(By.xpath(
                                "/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[6]/tbody/tr["
                                        + i + "]/td[2]"))
                        .getText());
                toAdd.setGroupsLink(driver.findElement(By.xpath(
                                "/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[6]/tbody/tr["
                                        + i + "]/td[1]/a"))
                        .getAttribute("href"));
                subjects.add(toAdd);
                i++;
            } catch (org.openqa.selenium.NoSuchElementException e) {

                if (j < pagination.size()) {
                    pagination.get(j).click();
                    j++;
                    i = 2;
                } else {
                    break;
                }

            }

        }

        for (i = 0; i < subjects.size(); i++) {

            int pageNum = 0;
            driver.get(subjects.get(i).getGroupsLink());
            int it = 4;
            while (true) {
                try {
                    // System.out.println("IT: " + it);
                    Group toAdd = new Group();

                    if (driver.findElement(By.xpath(
                                    "//*[@id='GORAPORTALU']/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr["
                                            + it + "]/td[1]"))
                            .getText().equals(""))
                        it++;

                    toAdd.setCode(driver.findElement(By.xpath(
                                    "//*[@id='GORAPORTALU']/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr["
                                            + it + "]/td[1]"))
                            .getText());
                    toAdd.setGroupName(driver.findElement(By.xpath(
                                    "//*[@id='GORAPORTALU']/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr["
                                            + it + "]/td[2]"))
                            .getText());
                    toAdd.setTeacher(driver.findElement(By.xpath(
                                    "//*[@id='GORAPORTALU']/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr["
                                            + (it + 1) + "]/td[1]"))
                            .getText());

                    toAdd.setDate(driver.findElement(By.xpath(
                                    "//*[@id='GORAPORTALU']/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr["
                                            + (it + 2) + "]/td/table/tbody/tr[1]/td"))
                            .getText());
                    toAdd.setForm(driver.findElement(By.xpath(
                                    "//*[@id='GORAPORTALU']/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr["
                                            + (it + 1) + "]/td[2]"))
                            .getText());
                    // System.out.println("Adding " + toAdd);

                    it += 3;
                    subjects.get(i).getGroups().add(toAdd);
                    // System.out.println("Is page 2 visible? " +
                    // driver.findElements(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr[41]/td/table/tbody/tr/td/span/input[2]")).isEmpty());
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    // e.printStackTrace();
                    ArrayList<WebElement> list = new ArrayList<WebElement>(driver.findElements(By.xpath(
                            "/html/body/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[3]/table/tbody/tr/td/table[7]/tbody/tr[41]/td/table/tbody/tr/td/span/input[2]")));

                    if (pageNum < list.size()) {
                        it = 4;
                        // System.out.println("List: " + list);
                        list.get(pageNum).click();
                        pageNum++;
                        // System.out.println("Switching page on subject " + subjects.get(i).getId());

                    } else {
                        break;
                    }
                }

            }

        }

        return subjects;
    }

}
