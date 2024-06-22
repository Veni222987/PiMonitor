package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.pi.server.mapper.HostMapper;
import org.pi.server.mapper.TeamMapper;
import org.pi.server.mapper.TeamUserMapper;
import org.pi.server.mapper.UserMapper;
import org.pi.server.model.entity.Host;
import org.pi.server.model.entity.Team;
import org.pi.server.model.entity.TeamUser;
import org.pi.server.model.entity.User;
import org.pi.server.service.AliyunOssService;
import org.pi.server.service.RedisService;
import org.pi.server.service.TeamService;
import org.pi.server.utils.CodeUtils;
import org.pi.server.utils.JwtUtils;
import org.pi.server.utils.QRCodeUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hu1hu
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    private final TeamUserMapper teamUserMapper;
    private final RedisService redisService;
    private final QRCodeUtils qrCodeUtils;
    private final HostMapper hostMapper;
    private final UserMapper userMapper;
    private final AliyunOssService aliyunOssService;
    private final TeamMapper teamMapper;

    /**
     * 创建团队
     * @param userID 用户ID
     * @param teamName 团队名
     * @return team
     */
    @Transactional
    @Override
    public Team create(Long userID, String teamName) {
        Team team = new Team();
        team.setName(teamName);
        team.setOwner(userID);
        team.setCreateTime(LocalDateTime.now());
        // 占位
        team.setToken("$");
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
                return team;
            }
        }
        return null;
    }

    /**
     * 修改团队
     * @param userID 用户ID
     * @param teamID 团队ID
     * @param teamName 团队名
     * @return 是否成功
     */
    @Override
    public boolean modify(String userID, String teamID, String teamName) {
        // 只有 owner 可以修改
        UpdateWrapper<Team> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", Long.parseLong(teamID)).eq("owner", Long.parseLong(userID));
        updateWrapper.set("name", teamName);
        return update(updateWrapper);
    }

    /**
     * 获取团队列表
     * @param userID 用户ID
     * @return 团队列表
     */
    @Override
    public IPage<Team> list(String userID, int page, int size) {
        // 第一步：分页查询 TeamUser 表
        QueryWrapper<TeamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.parseLong(userID));
        queryWrapper.select("team_id");

        // 获取 TeamUser 分页结果
        IPage<TeamUser> teamUserPage = teamUserMapper.selectPage(new Page<>(page, size), queryWrapper);
        List<Integer> teamIds = teamUserPage.getRecords().stream()
                .map(TeamUser::getTeamId)
                .collect(Collectors.toList());

        if (teamIds.isEmpty()) {
            // 如果没有 team_id，则返回空的分页对象
            return new Page<>();
        }

        // 第二步：根据 team_id 列表查询 Team 表
        QueryWrapper<Team> teamQueryWrapper = new QueryWrapper<>();
        teamQueryWrapper.in("id", teamIds);

        // 获取 Team 分页结果
        IPage<Team> teamPage = teamMapper.selectPage(new Page<>(page, size), teamQueryWrapper);
        teamPage.setTotal(teamUserPage.getTotal());
        teamPage.setCurrent(teamUserPage.getCurrent());
        return teamPage;
    }

    /**
     * 获取团队成员列表
     * @param userID 用户ID
     * @param teamID 团队ID
     * @param page 页码
     * @param size 大小
     * @return 成员列表 、 null 无权限
     */
    @Override
    public IPage<Map<String, Object>> members(String userID, String teamID, int page, int size) {
        QueryWrapper<TeamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamID);
        queryWrapper.eq("user_id", userID);
        if (!teamUserMapper.exists(queryWrapper)) {
            // 无权限
            return null;
        }
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamID);
        IPage<TeamUser> teamUserPage = teamUserMapper.selectPage(new Page<>(page, size), queryWrapper);
        List<Long> userIds = teamUserPage.getRecords().stream()
                .map(TeamUser::getUserId)
                .collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return new Page<>();
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIds);
        IPage<User> userPage = userMapper.selectPage(new Page<>(page, size), userQueryWrapper);
        // 将 IPage<User> 转换为 IPage<Map<String, Object>>
        IPage<Map<String, Object>> resultPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<Map<String, Object>> records = userPage.getRecords().stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getId());
                    map.put("userName", user.getUsername());
                    String avatar = user.getAvatar();
                    if (avatar != null && !avatar.startsWith("http")) {
                        avatar = aliyunOssService.generateSignedURL(avatar);
                    }
                    map.put("avatar", user.getAvatar());
                    return map;
                })
                .collect(Collectors.toList());
        resultPage.setRecords(records);
        resultPage.setTotal(teamUserPage.getTotal());
        resultPage.setCurrent(teamUserPage.getCurrent());
        return resultPage;
    }

    /**
     * 获取团队信息
     * @param userID 用户ID
     * @param teamID 团队ID
     * @return 团队信息
     */
    @Override
    public Team info(String userID, String teamID) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", teamID).eq("owner", userID);
        return getOne(queryWrapper);
    }

    /**
     * 删除团队
     * @param userID 用户ID
     * @param teamID 团队ID
     * @return 是否成功
     */
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

    /**
     * 邀请用户加入团队
     * @param userID 用户ID
     * @param type 邀请方式
     * @param teamID 团队ID
     * @return 邀请返回
     * @throws Exception 异常
     */
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
        // 3天过期
        redisService.set(code , code, 259200);
        String result = "no_type";
        switch (type) {
            case "QRCode" -> {
                // 生成 重定向链接
                String link = "http://120.77.76.40:8000/api/v1/team/invite/callback?code=" + code;
                result = qrCodeUtils.create(link, "src/main/resources/static/logo.jpg", true);
            }
            case "Code" -> {
                result = code;
            }
            case "Link" -> {
                result = "http://120.77.76.40:8000/api/v1/team/invite/callback?code=" + code;
            }
        }
        return result;
    }

    /**
     * 邀请回调
     * @param userID 用户ID
     * @param code 邀请码
     * @return 邀请结果
     */
    @Override
    public String inviteCallback(String userID, String code) {
        // 邀请码不存在
        if (redisService.get(code) == null) {
            return "no_code";
        } else {
            // 邀请码存在
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
