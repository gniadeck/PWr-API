package dev.wms.pwrapi.service.studentStats;

import dev.wms.pwrapi.dao.auth.UsosAuthDao;
import dev.wms.pwrapi.dao.usos.UsosDataDAO;
import dev.wms.pwrapi.model.studentStats.StudentStatsPersonalData;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentStatsPersonalDataService {

    private final UsosDataDAO usosDataDAO;
    private final UsosAuthDao usosAuthDao;

    public StudentStatsPersonalData getPersonalData(String login, String password){
        var client = usosAuthDao.login(login, password);

        var usosData = usosDataDAO.getStudies(client);
        var user = usosDataDAO.getUsosUser(client);
        var indexNumber = getStudentIndex(client);

        return StudentStatsPersonalData.builder()
                .firstName(user.first_name())
                .lastName(user.last_name())
                .currentFaculty(usosData.get(0).level())
                .currentMajor(usosData.get(0).name())
                .studiesType(usosData.get(0).type())
                .studentStatus(user.student_status())
                .phdStudentStatus(user.phd_student_status())
                .usosProfileUrl(user.profile_url())
                .photoUrl(user.photo_urls().values().iterator().next())
                .indexNumber(indexNumber)
                .build();
    }

    public Integer getStudentIndex(HttpClient client){
        var doc = usosDataDAO.getMyUsosWebPage(client);
        var infoFrame = doc.getElementById("info-frame");

        if(infoFrame == null)
            return null;

        var index = infoFrame.select("div ul li b").first();

        try{
            return index == null ? null : Integer.parseInt(index.text().trim());
        } catch(NumberFormatException e){
            return null;
        }
    }
}
