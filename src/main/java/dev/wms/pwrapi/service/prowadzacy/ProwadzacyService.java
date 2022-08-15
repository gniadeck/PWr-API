package dev.wms.pwrapi.service.prowadzacy;

import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyResult;

public interface ProwadzacyService {
    String getWebsiteStatus();

    ProwadzacyResult getForTeacherQuery(String query, int offset);

    ProwadzacyResult getForRoomQuery(String building, String query, Integer offset);

    ProwadzacyResult getForSubjectQuery(String query, Integer offset);
}
