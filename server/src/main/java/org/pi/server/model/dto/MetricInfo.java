package org.pi.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricInfo {
    private String agentID;
    private LocalDateTime time;
    private Map<String, Object> metric;
}
