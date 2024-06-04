package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.pi.server.common.ResultUtils;
import org.pi.server.mapper.HostMapper;
import org.pi.server.mapper.TeamMapper;
import org.pi.server.mapper.TeamUserMapper;
import org.pi.server.model.entity.Host;
import org.pi.server.model.entity.Team;
import org.pi.server.model.entity.TeamUser;
import org.pi.server.service.RedisService;
import org.pi.server.service.TeamService;
import org.pi.server.utils.AliSmsUtils;
import org.pi.server.utils.CodeUtils;
import org.pi.server.utils.JwtUtils;
import org.pi.server.utils.QRCodeUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    private final TeamUserMapper teamUserMapper;
    private final RedisService redisService;
    private final QRCodeUtils qrCodeUtils;
    private final HostMapper hostMapper;

    @Transactional
    @Override
    public String create(Long userID, String teamName) {
        Team team = new Team();
        team.setName(teamName);
        team.setOwner(userID);
        team.setCreateTime(LocalDateTime.now());
        team.setToken("$"); // 占位
        if (this.save(team)) {
            // 9999年过期
            Long expire = Instant.now().plusSeconds(3600*24*365*9999L).toEpochMilli()/1000;
            String jwt = JwtUtils.generateJwt(Map.of("teamID", team.getId()), expire);
            team.setToken(jwt);
            if (updateById(team)) {
                TeamUser teamUser = new TeamUser();
                teamUser.setTeamId(team.getId());
                teamUser.setUserId(userID);
                teamUserMapper.insert(teamUser);
                return jwt;
            }
        }
        return null;
    }

    @Override
    public boolean modify(String userID, String teamID, String teamName) {
        // 只有 owner 可以修改
        UpdateWrapper<Team> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", Long.parseLong(teamID)).eq("owner", Long.parseLong(userID));
        updateWrapper.set("name", teamName);
        return update(updateWrapper);
    }

    @Override
    public List<Team> list(String userID) {
        QueryWrapper<TeamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.parseLong(userID));
        queryWrapper.select("team_id");
        List<TeamUser> teamUserList = teamUserMapper.selectList(queryWrapper);
        System.out.println(teamUserList.toString());
        if (teamUserList.isEmpty()) {
            return List.of();
        }
        List<Integer> teamIds = teamUserList.stream().map(TeamUser::getTeamId).toList();
        if (teamIds.isEmpty()) {
            return List.of();
        }
        System.out.println(teamIds);
        return listByIds(teamIds);
    }

    @Override
    public Team info(String userID, String teamID) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", teamID).eq("owner", userID);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional
    public boolean delete(String userID, String teamID) {
        if (removeById(teamID)) {
            {
                QueryWrapper<TeamUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("team_id", teamID);
                teamUserMapper.delete(queryWrapper);
            }
            // 删除 Host
            QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("team_id", teamID);
            hostMapper.delete(queryWrapper);
            return true;
        }
        return false;
    }

    @Override
    public String invite(String userID, String type, String teamID) throws Exception {
        // 谁可以邀请
        Team byId = getById(teamID);
        if (byId == null) {
            return "no_team";
        } else if (byId.getOwner() != Long.parseLong(userID)) {
            return "no_auth";
        }
        // 生成邀请码
        String code = teamID + "-" + CodeUtils.generateVerifyCode(20, "1234567890ABCDEFGHIJKLMNOPQRSTUVWSYZ");
        redisService.set(code , code, 259200); // 3天过期
        String result = "no_type";
        switch (type) {
            case "QRCode" -> {
                // 生成 重定向链接
                String link = "http://qupgn4xn.shenzhuo.vip:40920/api/v1/team/invite/callback?code=" + code;
                result = qrCodeUtils.create(link, "src/main/resources/static/logo.jpg", true);
            }
            case "Code" -> {
                result = code;
            }
            case "Link" -> {
                result = "http://qupgn4xn.shenzhuo.vip:40920/api/v1/team/invite/callback?code=" + code;
            }
        }
        return result;
    }

    @Override
    public String inviteCallback(String userID, String code) {
        if (redisService.get(code) == null) { // 邀请码不存在
            return "no_code";
        } else { // 邀请码存在
            String[] split = code.split("-");
            String teamID = split[0];
            TeamUser teamUser = new TeamUser();
            teamUser.setTeamId(Integer.parseInt(teamID));
            teamUser.setUserId(Long.parseLong(userID));
            QueryWrapper<TeamUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("team_id", teamUser.getTeamId()).eq("user_id", teamUser.getUserId());
            if (teamUserMapper.exists(queryWrapper)) {
                return "already_invited";
            }
            if (teamUserMapper.insert(teamUser) > 0) {
                return "success";
            }
        }
        return null;
    }
}
