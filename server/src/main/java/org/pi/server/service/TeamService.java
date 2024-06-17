package org.pi.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.pi.server.model.entity.Team;

import java.util.List;
import java.util.Map;

public interface TeamService {
    Team create(Long userID, String teamName);
    boolean modify(String userID, String teamID, String teamName);
    IPage<Team> list(String userID, int page, int size);

    IPage<Map<String, Object>> members(String userID, String teamID, int page, int size);
    Team info(String userID, String teamID);

    boolean delete(String userID, String teamID);

    String invite(String userID, String type, String teamID) throws Exception;

    String inviteCallback(String userID, String code);
}
