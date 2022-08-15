package dev.wms.pwrapi.service.edukacja;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.entity.edukacja.Subject;

import java.util.List;

public interface EduService {

    List<Subject> doFetchSubjects(String login, String password) throws JsonProcessingException;
}
