package org.pi.server.service;

import org.pi.server.model.entity.Team;

import java.util.List;

public interface TeamService {
    String create(Long userID, String teamName);
    boolean modify(String userID, String teamID, String teamName);
    List<Team> list(String userID);
    Team info(String userID, String teamID);

    boolean delete(String userID, String teamID);

    String invite(String userID, String type, String teamID) throws Exception;

    String inviteCallback(String userID, String code);
}
