package dev.wms.pwrapi.dto.isjsosdown;


public record TrafficStatsDTO(int totalVisitsThisMonth,
                              double bounceRatePercentage,
                              double pagesPerVisit,
                              Long averageVisitDurationMillis) {

}
