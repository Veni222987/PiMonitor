package org.pi.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPUInfo {
    String cpuName;
    Integer core;
    Double frequency;
}
