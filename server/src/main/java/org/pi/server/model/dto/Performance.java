package org.pi.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Performance {
    private String cpuPercent;
    private String memPercent;
    private String diskPercent;
    private String tcpConnection;
    private String networkRate;
    private LocalDateTime time;
    private String agentID;
}
