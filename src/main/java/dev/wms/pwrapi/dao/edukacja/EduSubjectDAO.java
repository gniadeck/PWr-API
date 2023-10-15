package dev.wms.pwrapi.dao.edukacja;

import dev.wms.pwrapi.entity.edukacja.Subject;

import java.util.List;

public interface EduSubjectDAO {

    List<Subject> doFetchSubjects(String login, String password);
}
