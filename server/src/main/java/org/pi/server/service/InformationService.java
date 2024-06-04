package org.pi.server.service;

import java.util.List;
import java.util.Map;

public interface InformationService {
    Map<String, List<Map<String, Object>>> getPerformance(String userID, String agentID, Long startTime, Long endTime);
    Map<String, List<Map<String, Object>>> getMetric(String userID, String agentID, Long startTime, Long endTime);

    void updateStatusByScanTime(String agentID);

}
