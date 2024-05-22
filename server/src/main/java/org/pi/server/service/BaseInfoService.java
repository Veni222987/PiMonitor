package org.pi.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.pi.server.model.entity.Host;

public interface BaseInfoService extends IService<Host> {
    long postComputerInfo(Host host);

    boolean putComputerInfo(String agentID, String hostname);
}
