package dev.wms.pwrapi.service.studentStats;

import dev.wms.pwrapi.model.studentStats.StudentStatsData;

public interface StudentStatsService {

    StudentStatsData getStaticMockedData();
    StudentStatsData getDynamicMockedData(Integer numOfBlocks);
    StudentStatsData get(String login, String password);
}
