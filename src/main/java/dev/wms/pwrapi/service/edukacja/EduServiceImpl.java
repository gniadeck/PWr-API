package dev.wms.pwrapi.service.edukacja;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dao.edukacja.EduSubjectDAO;
import dev.wms.pwrapi.entity.edukacja.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EduServiceImpl implements EduService {

    private final EduSubjectDAO eduSubjectDAO;

    @Override
    public List<Subject> doFetchSubjects(String login, String password) throws JsonProcessingException {
        return eduSubjectDAO.doFetchSubjects(login, password);
    }
}
