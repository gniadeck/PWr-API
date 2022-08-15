package dev.wms.pwrapi.service.prowadzacy;

import dev.wms.pwrapi.dao.prowadzacy.ProwadzacyDAO;
import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProwadzacyServiceImpl implements ProwadzacyService {

    private ProwadzacyDAO prowadzacyDAO;

    @Autowired
    public ProwadzacyServiceImpl(ProwadzacyDAO prowadzacyDAO){
        this.prowadzacyDAO = prowadzacyDAO;
    }

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
