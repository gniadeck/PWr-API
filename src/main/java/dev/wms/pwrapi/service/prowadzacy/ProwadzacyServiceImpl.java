package dev.wms.pwrapi.service.prowadzacy;

import dev.wms.pwrapi.dao.prowadzacy.ProwadzacyDAO;
import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProwadzacyServiceImpl implements ProwadzacyService {

    private final ProwadzacyDAO prowadzacyDAO;

    @Override
    public String getWebsiteStatus(){
        return prowadzacyDAO.connectToWebsite();
    }

    @Override
    public ProwadzacyResult getForTeacherQuery(String query, int offset){
            return prowadzacyDAO.getPlanForTeacherQuery(query, offset);
    }

    @Override
    public ProwadzacyResult getForRoomQuery(String building, String query, Integer offset){
            return prowadzacyDAO.getPlanForRoomQuery(building, query, offset);
    }

    @Override
    public ProwadzacyResult getForSubjectQuery(String query, Integer offset){
            return prowadzacyDAO.getPlanForSubjectQuery(query, offset);
    }

}
