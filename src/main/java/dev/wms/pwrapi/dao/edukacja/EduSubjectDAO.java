package dev.wms.pwrapi.dao.edukacja;

import dev.wms.pwrapi.entity.edukacja.Subject;

import java.util.ArrayList;

public interface EduSubjectDAO {

    ArrayList<Subject> doFetchSubjects(String login, String password);
}
