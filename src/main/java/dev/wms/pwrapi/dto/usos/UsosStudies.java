package dev.wms.pwrapi.dto.usos;

import lombok.Builder;

import java.util.List;

@Builder
public record UsosStudies(String name, String level, String type, List<UsosSemester> semesters) {

    public UsosSemester lastSemester() {
        return semesters.get(0);
    }

    public UsosSemester firstSemester() {
        return semesters.get(semesters.size()-1);
    }

    public Integer numberOfSemesters() {
        return semesters.size();
    }

}
