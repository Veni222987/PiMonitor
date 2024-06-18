package org.pi.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.pi.server.model.entity.Host;

import java.util.List;

public interface BaseInfoService extends IService<Host> {
    long postComputerInfo(Host host);

    boolean putComputerInfo(String userID ,String agentID, String hostname);

    Host getComputerInfo(String userID, String agentID);

    IPage<Host> getList(String userID, String teamID, int page, int size);
}
