package dev.wms.pwrapi.model.studentStats;

import dev.wms.pwrapi.dto.usos.UsosStudentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentStatsPersonalData {

    private String firstName;
    private String lastName;
    private String currentFaculty;
    private String currentMajor;
    private Integer semester;
    private Integer currentStageOfStudies;
    private UsosStudentStatus studentStatus;
    private UsosStudentStatus phdStudentStatus;
    private String usosProfileUrl;
    private String photoUrl;
    private String studiesType;
    private Integer indexNumber;
}
