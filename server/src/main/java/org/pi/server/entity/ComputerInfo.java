package org.pi.server.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerInfo {
    Integer id;
    String macAddr;
    CPUInfo cpuInfo;
    Integer memory;
    Double disk;
    List<String> networkCard;
    String os;
}
